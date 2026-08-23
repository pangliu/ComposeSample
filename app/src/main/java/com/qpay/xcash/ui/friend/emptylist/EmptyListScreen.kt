package com.qpay.xcash.ui.friend.emptylist

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.qpay.xcash.R
import com.qpay.xcash.ui.friend.components.FriendTopBar
import com.qpay.xcash.ui.theme.AppTheme
import com.qpay.xcash.ui.theme.BlackGoldColors
import com.qpay.xcash.ui.theme.LocalAppColors
import com.qpay.xcash.ui.theme.NeonColors

@Composable
fun EmptyListScreen(
    onBack: () -> Unit,
    onAddFriend: () -> Unit = {}
) {
    val colors = LocalAppColors.current
    Scaffold(
        containerColor = colors.bg.page,
        contentColor = Color.White
    ) { paddingValues ->
        EmptyListContent(
            paddingValues = paddingValues,
            onBack = onBack,
            onAddFriend = onAddFriend
        )
    }
}

@Composable
private fun EmptyListContent(
    paddingValues: PaddingValues,
    onBack: () -> Unit,
    onAddFriend: () -> Unit = {}
) {
    val colors = LocalAppColors.current.emptyList
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        FriendTopBar(title = stringResource(R.string.friend_title), onBack = onBack)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .border(width = 1.5.dp, color = colors.iconCircleBorder, shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
//                    imageVector = Icons.Outlined.Person,
                    painter = painterResource(R.mipmap.ic_essent_friend),
                    contentDescription = null,
                    tint = colors.iconTint,
                    modifier = Modifier.size(40.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = stringResource(R.string.empty_list_friends_title),
                color = colors.titleText,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.empty_list_friends_subtitle),
                color = colors.subtitleText,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(28.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .background(brush = colors.actionButtonFill, shape = RoundedCornerShape(10.dp))
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { onAddFriend() },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.empty_list_add_friend),
                    color = colors.actionButtonText,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview(name = "Neon", showBackground = true, backgroundColor = 0xFF030F1B)
@Composable
private fun EmptyListScreenPreviewNeon() {
    AppTheme(colors = NeonColors) {
        EmptyListContent(paddingValues = PaddingValues(), onBack = {})
    }
}

@Preview(name = "Black Gold", showBackground = true, backgroundColor = 0xFF050505)
@Composable
private fun EmptyListScreenPreviewBlackGold() {
    AppTheme(colors = BlackGoldColors) {
        EmptyListContent(paddingValues = PaddingValues(), onBack = {})
    }
}
