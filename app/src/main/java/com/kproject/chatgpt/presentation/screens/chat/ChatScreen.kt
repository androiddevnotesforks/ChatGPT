package com.kproject.chatgpt.presentation.screens.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kproject.chatgpt.R
import com.kproject.chatgpt.presentation.model.Message
import com.kproject.chatgpt.presentation.model.RecentChat
import com.kproject.chatgpt.presentation.model.fakeChatList
import com.kproject.chatgpt.presentation.screens.components.EmptyListInfo
import com.kproject.chatgpt.presentation.screens.components.TopBar
import com.kproject.chatgpt.presentation.theme.CompletePreview
import com.kproject.chatgpt.presentation.theme.PreviewTheme
import com.kproject.chatgpt.presentation.theme.SimplePreview

private val recentChat = RecentChat(
    chatName = "Android Questions",
    chatMode = false,
    usedTokens = 450
)

@Composable
fun ChatScreen(
    chatId: Long,
    onNavigateBack: () -> Unit
) {
    val uiState = ChatUiState(
        messageList = fakeChatList,
        recentChat = recentChat
    )
    Content(
        uiState = uiState,
        onMessageValueChange = {},
        onSendMessage = {},
        onNavigateBack = onNavigateBack
    )
}

@Composable
private fun Content(
    modifier: Modifier = Modifier,
    uiState: ChatUiState,
    onMessageValueChange: (String) -> Unit,
    onSendMessage: (String) -> Unit,
    onNavigateBack: () -> Unit
) {
    Column {
        CustomTopBar(
            recentChat = uiState.recentChat,
            onNavigateBack = onNavigateBack
        )

        ChatList(
            chatList = uiState.messageList,
            modifier = Modifier
                .weight(1f)
        )
        Spacer(Modifier.height(8.dp))
        ChatTextField(
            message = uiState.message,
            onMessageValueChange = { message ->
                onMessageValueChange.invoke(message)
            },
            onSendMessage = { message ->
                onSendMessage.invoke(message)
            },
            modifier = Modifier.padding(12.dp)
        )
    }
}

@Composable
private fun CustomTopBar(
    modifier: Modifier = Modifier,
    recentChat: RecentChat,
    onNavigateBack: () -> Unit
) {
    val chatIcon = if (recentChat.chatMode) R.drawable.ic_chat else R.drawable.ic_manage_search
    TopBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    imageVector = ImageVector.vectorResource(id = chatIcon),
                    contentDescription = null,
                    contentScale = ContentScale.Inside,
                    colorFilter = ColorFilter.tint(MaterialTheme.colors.onSurface),
                    modifier = Modifier.size(40.dp)
                )
                Spacer(Modifier.width(6.dp))
                Column {
                    Text(
                        text = recentChat.chatName,
                        color = MaterialTheme.colors.onSurface,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        modifier = Modifier
                            .fillMaxWidth()
                    )

                    Text(
                        text = stringResource(
                            id = R.string.used_tokens,
                            recentChat.usedTokens.toString()
                        ),
                        color = MaterialTheme.colors.onSurface,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                        maxLines = 1,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
            }
        },
        navigationIcon = {
            IconButton(onClick = onNavigateBack) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = null,
                    tint = MaterialTheme.colors.onSurface
                )
            }
        },
        actions = {
            IconButton(
                onClick = {

                }
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = null,
                    tint = MaterialTheme.colors.onSurface
                )
            }
        },
        modifier = modifier
    )
}

@Composable
private fun ChatList(
    modifier: Modifier = Modifier,
    chatList: List<Message>
) {
    if (chatList.isNotEmpty()) {
        val lazyListState = rememberLazyListState()

        /**
         * Always scroll to the last message when opening the screen or when
         * the list size changes (sends or receives a new message).
         */
        LaunchedEffect(key1 = chatList.size) {
            lazyListState.scrollToItem(chatList.size)
        }

        LazyColumn(
            state = lazyListState,
            modifier = modifier.fillMaxSize()
        ) {
            itemsIndexed(chatList) { index, chat ->
                ChatListItem(
                    chat = chat
                )
            }
        }
    } else {
        EmptyListInfo(
            iconResId = R.drawable.ic_chat,
            title = stringResource(id = R.string.info_title_empty_chat_list),
            description = stringResource(id = R.string.info_description_empty_chat_list),
            modifier = modifier
        )
    }
}

@Composable
private fun ChatListItem(
    modifier: Modifier = Modifier,
    chat: Message
) {
    val backgroundColor = if (chat.sentByUser) Color.DarkGray else MaterialTheme.colors.secondary
    val alignment = if (chat.sentByUser) Alignment.Start else Alignment.End
    val shape = if (chat.sentByUser) {
        RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp, bottomStart = 20.dp, bottomEnd = 10.dp)
    } else {
        RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp, bottomStart = 8.dp, bottomEnd = 20.dp)
    }

    Column(
        modifier =
        modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 12.dp)
    ) {
        SelectionContainer {
            Text(
                text = chat.message,
                color = Color.White,
                fontSize = 16.sp,
                modifier = Modifier
                    .background(
                        color = backgroundColor,
                        shape = shape
                    )
                    .align(alignment)
                    .padding(8.dp)
            )
        }

        Text(
            text = chat.sendDate.toString(),
            color = MaterialTheme.colors.onPrimary,
            maxLines = 1,
            fontSize = 13.sp,
            modifier = Modifier
                .padding(start = 6.dp, end = 6.dp)
                .align(alignment)
        )
    }
}

@Composable
private fun ChatTextField(
    modifier: Modifier = Modifier,
    message: String,
    onMessageValueChange: (String) -> Unit,
    onSendMessage: (String) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxWidth()
    ) {
        TextField(
            value = message,
            onValueChange = { value ->
                onMessageValueChange.invoke(value)
            },
            textStyle = TextStyle(
                color = MaterialTheme.colors.onPrimary,
                fontSize = 18.sp
            ),
            placeholder = {
                Text(
                    text = stringResource(id = R.string.message),
                    color = MaterialTheme.colors.secondary
                )
            },
            maxLines = 4,
            shape = CircleShape,
            colors = TextFieldDefaults.textFieldColors(
                cursorColor = MaterialTheme.colors.onPrimary,
                backgroundColor = MaterialTheme.colors.surface,
                leadingIconColor = Color.White,
                trailingIconColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                focusedLabelColor = MaterialTheme.colors.secondary,
                unfocusedLabelColor = MaterialTheme.colors.onSecondary,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )

        Spacer(Modifier.width(8.dp))

        IconButton(
            onClick = { onSendMessage.invoke(message) },
            enabled = message.isNotBlank(),
            modifier = Modifier.background(
                color = MaterialTheme.colors.secondary,
                shape = CircleShape
            )
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_send),
                contentDescription = null,
                tint = Color.White
            )
        }
    }
}

@SimplePreview
@Composable
private fun Preview() {
    PreviewTheme {
        val uiState = ChatUiState(
            messageList = fakeChatList,
            recentChat = recentChat
        )
       Content(
           uiState = uiState,
           onMessageValueChange = {},
           onSendMessage = {},
           onNavigateBack = {}
       )
    }
}

@CompletePreview
@Composable
private fun CustomTopBarPreview() {
    PreviewTheme {
        CustomTopBar(recentChat = recentChat, onNavigateBack = {})
    }
}