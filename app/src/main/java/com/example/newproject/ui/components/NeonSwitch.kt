package com.example.newproject.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.newproject.ui.theme.neonCyan
import com.example.newproject.ui.theme.neonPurple
import com.example.newproject.ui.theme.welcomeBackground

@Composable
fun NeonSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    activeColor: Color = neonPurple
) {
    Switch(
        checked = checked,
        onCheckedChange = onCheckedChange,
        colors = SwitchDefaults.colors(
            checkedThumbColor = activeColor,
            checkedTrackColor = activeColor.copy(0.3f),
            checkedBorderColor = activeColor.copy(alpha = 0.7f),
            uncheckedThumbColor = activeColor.copy(alpha = 0.2f),
            uncheckedTrackColor = activeColor.copy(alpha = 0.1f),
            uncheckedBorderColor = activeColor.copy(alpha = 0.2f)
        )
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF0A0E1A)
@Composable
private fun NeonSwitchPreview() {
    var checkedPurple by remember { mutableStateOf(true) }
    var checkedCyan   by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .background(welcomeBackground)
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        NeonSwitch(checked = checkedPurple, onCheckedChange = { checkedPurple = it }, activeColor = neonPurple)
        NeonSwitch(checked = checkedCyan,   onCheckedChange = { checkedCyan = it },   activeColor = neonCyan)
    }
}
