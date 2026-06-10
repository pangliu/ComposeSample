package com.example.newproject.ui.profile.edit

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.ChildCare
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Wc
import androidx.compose.material.icons.filled.Work
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.School
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.newproject.R
import com.example.newproject.ui.components.SubPageTopBar
import com.example.newproject.ui.components.neonGlow
import com.example.newproject.ui.theme.balanceGold
import com.example.newproject.ui.theme.neonBlue
import com.example.newproject.ui.theme.neonBlueLight
import com.example.newproject.ui.theme.neonCyan
import com.example.newproject.ui.theme.neonDarkPurple
import com.example.newproject.ui.theme.neonMint
import com.example.newproject.ui.theme.neonPurple
import com.example.newproject.ui.theme.normalText
import com.example.newproject.ui.theme.welcomeBackground

private val CardBackground = Color(0xFF0E1A2E)

@Composable
fun ProfileEditScreen(
    viewModel: ProfileEditViewModel = hiltViewModel(),
    onBack: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    ProfileEditContent(uiState = uiState, onBack = onBack)
}

@Composable
private fun ProfileEditContent(
    uiState: ProfileEditUiState = ProfileEditUiState(),
    onBack: () -> Unit = {}
) {
    Scaffold(
        containerColor = welcomeBackground,
        contentColor = Color.White
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            SubPageTopBar(
                title = stringResource(R.string.profile_edit),
                onBack = onBack
            )

            PromoBanner(modifier = Modifier.padding(horizontal = 16.dp))

            Spacer(Modifier.height(20.dp))

            MyDeetsSection(
                uiState = uiState,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(Modifier.height(20.dp))

            SpillTheTeaSection(
                uiState = uiState,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(Modifier.height(32.dp))
        }
    }
}

@Composable
private fun PromoBanner(modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxWidth()
//            .height(90.dp)
//            .neonGlow(color = neonPurple, alpha = 0.4f, glowRadius = 10.dp, borderRadius = 16.dp)
//            .background(
//                Brush.horizontalGradient(
//                    listOf(Color(0xFF200A40), Color(0xFF0D1630), Color(0xFF200A40))
//                ),
//                RoundedCornerShape(16.dp)
//            )
//            .border(1.5.dp, neonPurple.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
    ) {
        Image(
            painter = painterResource(R.mipmap.bg_profile_edit_top),
            contentDescription = null,
//            contentScale = ContentScale.Companion.FillWidth,
            contentScale = ContentScale.FillWidth                                                                           ,
            modifier = Modifier.Companion.fillMaxWidth()
        )
        Spacer(Modifier.width(10.dp))
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = stringResource(R.string.profile_edit_banner_title),
                color = balanceGold,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Text(
                text = stringResource(R.string.profile_edit_banner_subtitle),
                color = Color.White,
                fontSize = 18.sp,
                lineHeight = 17.sp
            )
        }
    }
}

@Composable
private fun MyDeetsSection(uiState: ProfileEditUiState, modifier: Modifier = Modifier) {
    SectionCard(
        title = stringResource(R.string.profile_edit_section_my_deets),
        borderColor = neonBlueLight,
        modifier = modifier
    ) {
        LockedFieldRow(
            icon = Icons.Default.Person,
            label = stringResource(R.string.profile_edit_full_name),
            value = uiState.fullName.ifEmpty { "---" }
        )
        FieldDivider()
        PlainFieldRow(
            icon = Icons.Default.Wc,
            label = stringResource(R.string.profile_edit_gender),
            value = uiState.gender
        )
        FieldDivider()
        PlainFieldRow(
            icon = Icons.Default.Phone,
            label = stringResource(R.string.profile_edit_mobile),
            value = uiState.mobile.ifEmpty { "---" }
        )
        FieldDivider()
        ChangeableFieldRow(
            icon = Icons.Default.Email,
            label = stringResource(R.string.profile_edit_email),
            value = uiState.email.ifEmpty { "---" }
        )
        FieldDivider()
        PasswordFieldRow()
        FieldDivider()
        LockedFieldRow(
            icon = Icons.Default.CalendarToday,
            label = stringResource(R.string.profile_edit_dob),
            value = uiState.dob
        )
    }
}

@Composable
private fun SpillTheTeaSection(uiState: ProfileEditUiState, modifier: Modifier = Modifier) {
    SectionCard(
        title = stringResource(R.string.profile_edit_section_spill_tea),
        borderColor = neonBlueLight,
        modifier = modifier
    ) {
        DropdownFieldRow(
            icon = Icons.Outlined.School,
            label = stringResource(R.string.profile_edit_education),
            value = uiState.educationLevel
        )
        FieldDivider()
        DropdownFieldRow(
            icon = Icons.Default.Work,
            label = stringResource(R.string.profile_edit_hustle),
            value = uiState.currentHustle
        )
        FieldDivider()
        DropdownFieldRow(
            icon = Icons.Default.People,
            label = stringResource(R.string.profile_edit_income_currency),
            value = uiState.incomeCurrency
        )
        FieldDivider()
        DropdownFieldRow(
            icon = Icons.Default.Favorite,
            label = stringResource(R.string.profile_edit_yearly_bag),
            value = uiState.yearlyBag
        )
        FieldDivider()
        DropdownFieldRow(
            icon = Icons.Default.Favorite,
            label = stringResource(R.string.profile_edit_relationship),
            value = uiState.relationshipStatus
        )
        FieldDivider()
        DropdownFieldRow(
            icon = Icons.Default.ChildCare,
            label = stringResource(R.string.profile_edit_kids),
            value = uiState.gotKids
        )
        FieldDivider()
        DropdownFieldRow(
            icon = Icons.Default.Search,
            label = stringResource(R.string.profile_edit_find_us),
            value = uiState.howFoundUs
        )
        FieldDivider()
        DropdownFieldRow(
            icon = Icons.Default.People,
            label = stringResource(R.string.profile_edit_referral),
            value = uiState.referralName,
            subtitle = stringResource(R.string.profile_edit_referral_code, uiState.referralCode)
        )
        Spacer(Modifier.height(4.dp))
    }
}

@Composable
private fun SectionCard(
    title: String,
    borderColor: Color = neonCyan,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Box(modifier = modifier.fillMaxWidth()) {
        // 先畫 chip（z-order 較低），讓卡片背景蓋住其下半段邊框
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
//                .background(
//                    color = neonDarkPurple.copy(alpha = 0.5f),
//                    shape = RoundedCornerShape(8.dp))
                .border(
                    width = 1.5.dp,
                    color = neonDarkPurple,
                    shape = RoundedCornerShape(8.dp))
                .neonGlow(
                    color = neonDarkPurple.copy(alpha = 0.8f),
                    glowRadius = 8.dp,
                    borderRadius = 14.dp
                )
                .padding(top = 8.dp, start = 8.dp, end = 8.dp, bottom = 25.dp)
        ) {
            Text(
                text = title,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }
        // 後畫卡片（z-order 較高），其不透明背景蓋住 chip 的下半段
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 33.dp)
                .neonGlow(borderColor, alpha = 0.6f, glowRadius = 8.dp, borderRadius = 14.dp)
                .background(CardBackground, RoundedCornerShape(14.dp))
                .border(1.5.dp, borderColor.copy(alpha = 0.5f), RoundedCornerShape(14.dp))
                .padding(top = 12.dp),
            content = content
        )
    }
}

@Composable
private fun FieldDivider() {
    HorizontalDivider(
        modifier = Modifier.padding(horizontal = 16.dp),
        color = neonCyan.copy(alpha = 0.12f),
        thickness = 0.5.dp
    )
}

@Composable
private fun LockedFieldRow(icon: ImageVector, label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = neonCyan, modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(10.dp))
        Text(label, color = normalText, fontSize = 13.sp, modifier = Modifier.weight(1f))
        Icon(
            imageVector = Icons.Outlined.Lock,
            contentDescription = null,
            tint = normalText.copy(alpha = 0.6f),
            modifier = Modifier.size(13.dp)
        )
        Spacer(Modifier.width(4.dp))
        Text(value, color = Color.White, fontSize = 13.sp)
    }
}

@Composable
private fun PlainFieldRow(icon: ImageVector, label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = neonCyan, modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(10.dp))
        Text(label, color = normalText, fontSize = 13.sp, modifier = Modifier.weight(1f))
        Text(value, color = Color.White, fontSize = 13.sp)
    }
}

@Composable
private fun ChangeableFieldRow(
    icon: ImageVector,
    label: String,
    value: String,
    onChangeTap: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = neonCyan, modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(10.dp))
        Text(label, color = normalText, fontSize = 13.sp, modifier = Modifier.weight(1f))
        Text(value, color = normalText, fontSize = 12.sp)
        Spacer(Modifier.width(8.dp))
        Box(
            modifier = Modifier
                .border(
                    width = 1.5.dp,
                    brush = Brush.linearGradient(
                        colors = listOf(
                            neonCyan.copy(alpha = 0.4f),
                            neonPurple.copy(alpha = 0.8f)
                        )),
                    shape = RoundedCornerShape(6.dp))
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { onChangeTap() }
                .padding(horizontal = 8.dp, vertical = 4.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.profile_edit_change),
                color = neonCyan,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun PasswordFieldRow(onChangeTap: () -> Unit = {}) {
    var isVisible by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Outlined.Lock,
            contentDescription = null,
            tint = neonCyan,
            modifier = Modifier.size(20.dp)
        )
        Spacer(Modifier.width(10.dp))
        Text(
            text = stringResource(R.string.profile_edit_password),
            color = normalText,
            fontSize = 13.sp,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = if (isVisible) "password" else "••••••••",
            color = Color.White,
            fontSize = 13.sp
        )
        Spacer(Modifier.width(6.dp))
        Icon(
            painter = painterResource(R.mipmap.ic_balance_eye),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier
                .size(18.dp)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { isVisible = !isVisible }
        )
        Spacer(Modifier.width(8.dp))
        Box(
            modifier = Modifier
                .border(
                    width = 1.5.dp,
                    brush = Brush.linearGradient(
                        colors = listOf(
                            neonCyan.copy(alpha = 0.4f),
                            neonPurple.copy(alpha = 0.8f)
                        )),
                    shape = RoundedCornerShape(6.dp))
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { onChangeTap() }
                .padding(horizontal = 8.dp, vertical = 4.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.profile_edit_change),
                color = neonCyan,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun DropdownFieldRow(
    icon: ImageVector,
    label: String,
    value: String,
    subtitle: String? = null,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onClick() }
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = neonCyan, modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(10.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(label, color = Color.White, fontSize = 13.sp)
            if (subtitle != null) {
                Text(subtitle, color = normalText, fontSize = 11.sp)
            }
        }
        Row(
            modifier = Modifier
                .border(
                    width = 1.5.dp,
                    brush = Brush.linearGradient(
                        colors = listOf(
                            neonCyan.copy(alpha = 0.4f),
                            neonPurple.copy(alpha = 0.8f)
                        )),
                    shape = RoundedCornerShape(6.dp))
//                .border(1.dp, neonCyan.copy(alpha = 0.6f), RoundedCornerShape(8.dp))
                .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(value, color = Color.White, fontSize = 12.sp)
            Spacer(Modifier.width(4.dp))
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                tint = neonCyan,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0B1327)
@Composable
private fun ProfileEditPreview() {
    MaterialTheme {
        ProfileEditContent(
            uiState = ProfileEditUiState(
                fullName = "Bruce Banner",
                gender = "Male",
                mobile = "0917-123-4567",
                email = "bruce@starklabs.com",
                dob = "1970-01-01"
            )
        )
    }
}
