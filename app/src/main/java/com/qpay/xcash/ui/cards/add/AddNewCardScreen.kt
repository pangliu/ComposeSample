package com.qpay.xcash.ui.cards.add

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.qpay.xcash.ui.cards.CardsNavigationEvent
import com.qpay.xcash.ui.cards.CardsViewModel
import com.qpay.xcash.ui.components.LoadingDialog
import com.qpay.xcash.ui.components.SubPageTopBar
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.qpay.xcash.R
import com.qpay.xcash.ui.Routes
import com.qpay.xcash.ui.UiEvent
import com.qpay.xcash.ui.theme.AppTheme
import com.qpay.xcash.ui.theme.BlackGoldAssets
import com.qpay.xcash.ui.theme.BlackGoldColors
import com.qpay.xcash.ui.theme.LocalAppColors
import com.qpay.xcash.ui.theme.NeonColors

private enum class CardInputMethod { OCR, GALLERY }

@Composable
fun AddNewCardScreen(
    onBack: () -> Unit,
    onNavigate: (String) -> Unit = {},
    viewModel: CardsViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.eventFlow.collect { event ->
            when (event) {
                is UiEvent.ShowToast -> Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                is UiEvent.ShowDialog -> Toast.makeText(context, event.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                is CardsNavigationEvent.CardLinkedSuccess -> onNavigate(Routes.CARD_LINKED_SUCCESS)
            }
        }
    }

    AddNewCardContent(
        onBack = onBack,
        isLoading = uiState.isAddingCard,
        onSubmit = { cardNumber, cardholderName, expiryDate, cvv, billingZip ->
            viewModel.addNewCard(cardNumber, cardholderName, expiryDate, cvv, billingZip)
        }
    )
}

@Composable
fun AddNewCardContent(
    onBack: () -> Unit = {},
    isLoading: Boolean = false,
    onSubmit: (cardNumber: String, cardholderName: String, expiryDate: String, cvv: String, billingZip: String) -> Unit = { _, _, _, _, _ -> }
) {
    var selectedMethod by rememberSaveable { mutableStateOf(CardInputMethod.GALLERY) }
    var cardNumber by rememberSaveable { mutableStateOf("") }
    var cardholderName by rememberSaveable { mutableStateOf("") }
    var expiryDate by rememberSaveable { mutableStateOf("") }
    var cvv by rememberSaveable { mutableStateOf("") }
    var billingZip by rememberSaveable { mutableStateOf("") }

    val colors = LocalAppColors.current

    LoadingDialog(isShowing = isLoading)

    Scaffold(
        containerColor = colors.bg.page,
        contentColor = Color.White
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            SubPageTopBar(
                title = stringResource(R.string.add_new_card_title),
                onBack = onBack
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // OCR / Gallery 選擇按鈕
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    InputMethodButton(
                        icon = painterResource(R.drawable.ic_ocr),
                        selected = selectedMethod == CardInputMethod.OCR,
                        onClick = { selectedMethod = CardInputMethod.OCR },
                        modifier = Modifier.weight(1f)
                    )
                    InputMethodButton(
                        icon = painterResource(R.drawable.ic_picture),
                        selected = selectedMethod == CardInputMethod.GALLERY,
                        onClick = { selectedMethod = CardInputMethod.GALLERY },
                        modifier = Modifier.weight(1f)
                    )
                }

                // Card Number
                CardFormField(
                    label = stringResource(R.string.add_new_card_card_number),
                    value = cardNumber,
                    onValueChange = { cardNumber = it },
                    placeholder = stringResource(R.string.add_new_card_card_number_hint),
                    keyboardType = KeyboardType.Number
                )

                // Cardholder Name
                CardFormField(
                    label = stringResource(R.string.add_new_card_cardholder_name),
                    value = cardholderName,
                    onValueChange = { cardholderName = it },
                    placeholder = stringResource(R.string.add_new_card_cardholder_name_hint)
                )

                // Expiry Date + CVV
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    CardFormField(
                        label = stringResource(R.string.add_new_card_expiry_date),
                        value = expiryDate,
                        onValueChange = { expiryDate = it },
                        placeholder = stringResource(R.string.add_new_card_expiry_hint),
                        keyboardType = KeyboardType.Number,
                        modifier = Modifier.weight(1f)
                    )
                    CardFormField(
                        label = stringResource(R.string.add_new_card_cvv),
                        value = cvv,
                        onValueChange = { if (it.length <= 4) cvv = it },
                        placeholder = stringResource(R.string.add_new_card_cvv_hint),
                        keyboardType = KeyboardType.Number,
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.weight(1f)
                    )
                }

                // Billing Zip Code
                CardFormField(
                    label = stringResource(R.string.add_new_card_billing_zip),
                    value = billingZip,
                    onValueChange = { billingZip = it },
                    placeholder = stringResource(R.string.add_new_card_billing_zip_hint),
                    keyboardType = KeyboardType.Number
                )

                // CVV notice
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Lock,
                        contentDescription = null,
                        tint = colors.text.body,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = stringResource(R.string.add_new_card_cvv_notice),
                        color = colors.text.body,
                        fontSize = 13.sp
                    )
                }

                // Confirm button
                Button(
                    onClick = { onSubmit(cardNumber, cardholderName, expiryDate, cvv, billingZip) },
                    enabled = !isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(45.dp)
                        .background(colors.addNewCard.confirmBackground, RoundedCornerShape(12.dp)),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = colors.addNewCard.confirmContent,
                        disabledContainerColor = Color.Transparent,
                        disabledContentColor = colors.addNewCard.confirmContent.copy(alpha = 0.5f)
                    )
                ) {
                    Text(
                        text = stringResource(R.string.add_new_card_confirm),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun InputMethodButton(
    icon: Painter,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = LocalAppColors.current
    val borderColor = if (selected) colors.addNewCard.methodBorderSelected else colors.addNewCard.methodBorderUnselected
    val bgColor = if (selected) colors.addNewCard.methodBackgroundSelected else colors.addNewCard.methodBackgroundUnselected
    Box(
        modifier = modifier
            .height(110.dp)
            .background(bgColor, RoundedCornerShape(12.dp))
            .border(1.5.dp, borderColor, RoundedCornerShape(12.dp))
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            modifier = Modifier.size(50.dp),
            painter = icon,
            contentDescription = null,
        )
    }
}

//@Composable
//private fun ScanFrameIcon(color: Color, modifier: Modifier = Modifier) {
//    Canvas(modifier = modifier) {
//        val cornerLen = size.width * 0.28f
//        val stroke = 2.5.dp.toPx()
//        val inset = stroke / 2
//
//        fun hLine(x1: Float, x2: Float, y: Float) =
//            drawLine(color, Offset(x1, y), Offset(x2, y), stroke, StrokeCap.Square)
//        fun vLine(x: Float, y1: Float, y2: Float) =
//            drawLine(color, Offset(x, y1), Offset(x, y2), stroke, StrokeCap.Square)
//
//        val r = size.width - inset
//        val b = size.height - inset
//
//        // 四個角落
//        vLine(inset, inset, inset + cornerLen);  hLine(inset, inset + cornerLen, inset)
//        hLine(r - cornerLen, r, inset);          vLine(r, inset, inset + cornerLen)
//        vLine(inset, b - cornerLen, b);          hLine(inset, inset + cornerLen, b)
//        hLine(r - cornerLen, r, b);              vLine(r, b - cornerLen, b)
//    }
//}

@Composable
private fun CardFormField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    val colors = LocalAppColors.current
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    val borderColor = if (isFocused) colors.addNewCard.fieldBorderFocused else colors.addNewCard.fieldBorderUnfocused
    val borderWidth = 1.5.dp

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
            text = label,
            color = colors.addNewCard.fieldLabel,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )

        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = TextStyle(color = colors.addNewCard.fieldText, fontSize = 16.sp),
            singleLine = true,
            visualTransformation = visualTransformation,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            interactionSource = interactionSource,
            cursorBrush = SolidColor(colors.addNewCard.fieldCursor),
            modifier = Modifier.fillMaxWidth(),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(colors.addNewCard.fieldBackground, RoundedCornerShape(8.dp))
                        .border(borderWidth, borderColor, RoundedCornerShape(8.dp))
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (value.isEmpty()) {
                        Text(
                            text = placeholder,
                            color = colors.addNewCard.fieldPlaceholder,
                            fontSize = 14.sp
                        )
                    }
                    innerTextField()
                }
            }
        )
    }
}

@Preview(name = "Neon", showBackground = true, backgroundColor = 0xFF030F1B)
@Composable
private fun AddNewCardPreviewNeon() {
    AppTheme(colors = NeonColors) {
        AddNewCardContent()
    }
}

@Preview(name = "Black Gold", showBackground = true, backgroundColor = 0xFF050505)
@Composable
private fun AddNewCardPreviewBlackGold() {
    AppTheme(colors = BlackGoldColors, assets = BlackGoldAssets) {
        AddNewCardContent()
    }
}
