package com.qpay.xcash.ui.friend.addFriend

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.qpay.xcash.R
import com.qpay.xcash.ui.UiEvent
import com.qpay.xcash.ui.components.LoadingDialog
import com.qpay.xcash.ui.friend.components.FoundFriendItem
import com.qpay.xcash.ui.friend.components.FriendTopBar
import com.qpay.xcash.ui.theme.AppTheme
import com.qpay.xcash.ui.theme.BlackGoldColors
import com.qpay.xcash.ui.theme.LocalAppColors
import com.qpay.xcash.ui.theme.NeonColors

private val countryCodes = listOf("+63", "+886", "+1", "+65", "+60")

@Composable
fun AddFriendScreen(onBack: () -> Unit, viewModel: AddFriendViewModel = hiltViewModel()) {
    val colors = LocalAppColors.current
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.eventFlow.collect { event ->
            when (event) {
                is UiEvent.ShowToast -> Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                is UiEvent.ShowDialog -> Unit
            }
        }
    }

    Scaffold(
        containerColor = colors.bg.page,
        contentColor = Color.White
    ) { paddingValues ->
        AddFriendContent(
            paddingValues = paddingValues,
            uiState = uiState,
            onBack = onBack,
            onFindFriend = viewModel::findFriend,
            onAddFoundFriend = viewModel::onAddFriendClick,
            onDismissFoundFriend = viewModel::clearFoundFriend
        )
    }

    LoadingDialog(isShowing = uiState.isLoading)
}

@Composable
private fun AddFriendContent(
    paddingValues: PaddingValues,
    uiState: AddFriendUiState = AddFriendUiState(),
    onBack: () -> Unit = {},
    onFindFriend: (String, String) -> Unit = { _, _ -> },
    onAddFoundFriend: () -> Unit = {},
    onDismissFoundFriend: () -> Unit = {}
) {
    val colors = LocalAppColors.current.addFriend
    var countryCode by remember { mutableStateOf(countryCodes.first()) }
    var phoneNumber by remember { mutableStateOf("") }
    var isCountryMenuExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        FriendTopBar(title = stringResource(R.string.add_friend_title), onBack = onBack)

        Spacer(Modifier.height(20.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .background(colors.sectionDot, CircleShape)
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.add_friend_phone_label),
                color = colors.sectionLabelText,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(Modifier.height(10.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box {
                Row(
                    modifier = Modifier
                        .border(
                            width = 1.dp,
//                            color = colors.countryCodeBorder,
                            brush = colors.countryCodeBorder,
                            shape = RoundedCornerShape(24.dp))
                        .background(
                            color = colors.countryCodeBackground,
                            shape = RoundedCornerShape(24.dp))
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) { isCountryMenuExpanded = true }
                        .padding(horizontal = 14.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = countryCode,
                        color = colors.countryCodeText,
                        fontSize = 14.sp
                    )
                    Spacer(Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = stringResource(R.string.add_friend_country_code_desc),
                        tint = colors.countryCodeChevron,
                        modifier = Modifier.size(18.dp)
                    )
                }
                DropdownMenu(
                    expanded = isCountryMenuExpanded,
                    onDismissRequest = { isCountryMenuExpanded = false }) {
                    countryCodes.forEach { code ->
                        DropdownMenuItem(
                            text = { Text(code) },
                            onClick = {
                                countryCode = code
                                isCountryMenuExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(Modifier.width(10.dp))

            Box(
                modifier = Modifier
                    .weight(1f)
                    .border(
                        width = 1.dp,
//                        color = colors.inputBorder,
                        brush = colors.inputBorder,
                        shape = RoundedCornerShape(24.dp))
                    .background(
                        color = colors.inputBackground,
                        shape = RoundedCornerShape(24.dp))
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                if (phoneNumber.isEmpty()) {
                    Text(
                        text = stringResource(R.string.add_friend_phone_hint),
                        color = colors.inputPlaceholderText,
                        fontSize = 14.sp
                    )
                }
                BasicTextField(
                    value = phoneNumber,
                    onValueChange = { phoneNumber = it },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    textStyle = TextStyle(color = colors.inputText, fontSize = 14.sp),
                    cursorBrush = SolidColor(colors.inputText),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(Modifier.width(10.dp))

            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = stringResource(R.string.add_friend_search_desc),
                tint = colors.searchIconTint,
                modifier = Modifier
                    .size(22.dp)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { onFindFriend(countryCode, phoneNumber) }
            )
        }

        uiState.foundFriend?.let { friend ->
            Spacer(Modifier.height(20.dp))
            FoundFriendItem(
                friend = friend,
                onAdd = onAddFoundFriend,
                onDismiss = onDismissFoundFriend,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}

@Preview(name = "Neon", showBackground = true, backgroundColor = 0xFF030F1B)
@Composable
private fun AddFriendScreenPreviewNeon() {
    AppTheme(colors = NeonColors) {
        AddFriendContent(paddingValues = PaddingValues(), onBack = {})
    }
}

@Preview(name = "Black Gold", showBackground = true, backgroundColor = 0xFF050505)
@Composable
private fun AddFriendScreenPreviewBlackGold() {
    AppTheme(colors = BlackGoldColors) {
        AddFriendContent(paddingValues = PaddingValues(), onBack = {})
    }
}
