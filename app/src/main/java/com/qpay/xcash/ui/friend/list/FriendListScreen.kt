package com.qpay.xcash.ui.friend.list

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.qpay.xcash.R
import com.qpay.xcash.network.model.response.ContactType
import com.qpay.xcash.network.model.response.FriendResponse
import com.qpay.xcash.ui.Routes
import com.qpay.xcash.ui.UiEvent
import com.qpay.xcash.ui.components.GradientText
import com.qpay.xcash.ui.components.LoadingDialog
import com.qpay.xcash.ui.friend.components.FriendListItem
import com.qpay.xcash.ui.friend.components.FriendSortDialog
import com.qpay.xcash.ui.theme.AppTheme
import com.qpay.xcash.ui.theme.BlackGoldAssets
import com.qpay.xcash.ui.theme.BlackGoldColors
import com.qpay.xcash.ui.theme.LocalAppColors
import com.qpay.xcash.ui.theme.NeonColors

@Composable
fun FriendListScreen(
    viewModel: FriendListViewModel,
    onBack: () -> Unit,
    onNavigate: (String) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.eventFlow.collect { event ->
            when (event) {
                is UiEvent.ShowToast -> Toast.makeText(context, event.message, Toast.LENGTH_SHORT)
                    .show()

                is UiEvent.ShowDialog -> Toast.makeText(context, event.message, Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    val colors = LocalAppColors.current
    Scaffold(
        containerColor = colors.bg.page,
        contentColor = Color.White
    ) { paddingValues ->
        LoadingDialog(isShowing = uiState.isLoading)
        FriendListContent(
            uiState = uiState,
            paddingValues = paddingValues,
            onBack = onBack,
            onAddFriendClick = viewModel::onAddFriendClick,
            onSearchQueryChange = viewModel::onSearchQueryChange,
            onTabSelected = viewModel::onTabSelected,
            onCategorySelected = viewModel::onCategorySelected,
            onSortOrderSelected = viewModel::onSortOrderSelected,
            onToggleFavorite = viewModel::onToggleFavorite,
            onRemoveFriend = viewModel::onRemoveFriend,
            onFriendClick = { friend ->
                viewModel.selectFriend(friend)
                onNavigate(Routes.friendDetail(friend.id))
            }
        )
    }
}

@Composable
private fun FriendListContent(
    uiState: FriendListUiState,
    paddingValues: PaddingValues,
    onBack: () -> Unit,
    onAddFriendClick: () -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onTabSelected: (FriendListTab) -> Unit,
    onCategorySelected: (String?) -> Unit,
    onSortOrderSelected: (FriendListSortOrder) -> Unit,
    onToggleFavorite: (String) -> Unit,
    onRemoveFriend: (String) -> Unit,
    onFriendClick: (FriendResponse) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        FriendListTopBar(onBack = onBack, onAddFriendClick = onAddFriendClick)

        FriendListSearchBar(
            query = uiState.searchQuery,
            onQueryChange = onSearchQueryChange,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(Modifier.height(16.dp))

        FriendListTabRow(
            selectedTab = uiState.selectedTab,
            onTabSelected = onTabSelected,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(Modifier.height(12.dp))

        FriendListCategoryChips(
            categories = uiState.categories,
            selectedCategory = uiState.selectedCategory,
            onCategorySelected = onCategorySelected,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(Modifier.height(16.dp))

        FriendListResultsHeader(
            resultCount = uiState.filteredFriends.size,
            sortOrder = uiState.sortOrder,
            onSortOrderSelected = onSortOrderSelected,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        val filteredFriends = uiState.filteredFriends
        if (filteredFriends.isEmpty() && !uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = stringResource(R.string.friend_empty),
                    color = LocalAppColors.current.text.body,
                    fontSize = 14.sp
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
            ) {
                items(filteredFriends, key = { it.id }) { friend ->
                    FriendListItem(
                        friend = friend,
                        onToggleFavorite = { onToggleFavorite(friend.id) },
                        onRemove = { onRemoveFriend(friend.id) },
                        onClick = { onFriendClick(friend) }
                    )
                }
            }
        }
    }
}

@Composable
private fun FriendListTopBar(
    onBack: () -> Unit,
    onAddFriendClick: () -> Unit
) {
    val colors = LocalAppColors.current
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBackIosNew,
            contentDescription = stringResource(R.string.common_back_desc),
            tint = colors.accent.primary,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 16.dp)
                .size(20.dp)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { onBack() }
        )
        GradientText(
            text = stringResource(R.string.friend_title),
            color = colors.accent.primary,
            brush = colors.gradient.goldShimmer,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Center)
        )
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = stringResource(R.string.friend_add_desc),
            tint = colors.accent.primary,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 16.dp)
                .size(28.dp)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { onAddFriendClick() }
        )
    }
}

@Composable
private fun FriendListSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalAppColors.current.friendList
    Row(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, colors.searchBarBorder.copy(alpha = 0.6f), RoundedCornerShape(24.dp))
            .background(colors.searchBarBackground, RoundedCornerShape(24.dp))
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = null,
            tint = colors.searchPlaceholderText,
            modifier = Modifier.size(20.dp)
        )
        Spacer(Modifier.width(10.dp))
        Box(modifier = Modifier.weight(1f)) {
            if (query.isEmpty()) {
                Text(
                    text = stringResource(R.string.friend_search_hint),
                    color = colors.searchPlaceholderText,
                    fontSize = 14.sp
                )
            }
            BasicTextField(
                value = query,
                onValueChange = onQueryChange,
                singleLine = true,
                textStyle = TextStyle(color = Color.White, fontSize = 14.sp),
                cursorBrush = SolidColor(Color.White),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun FriendListTabRow(
    selectedTab: FriendListTab,
    onTabSelected: (FriendListTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalAppColors.current.friendList
    val tabs = listOf(
        FriendListTab.ALL to stringResource(R.string.friend_tab_all),
        FriendListTab.FAVORITES to stringResource(R.string.friend_tab_favorites),
        FriendListTab.RECENT to stringResource(R.string.friend_tab_recent)
    )
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(28.dp)
    ) {
        tabs.forEach { (tab, label) ->
            val isSelected = tab == selectedTab
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .weight(1f)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { onTabSelected(tab) }
            ) {
                Text(
                    text = label,
                    color = if (isSelected) colors.tabSelectedText else colors.tabUnselectedText,
                    fontSize = 15.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                )
                Spacer(Modifier.height(6.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(2.dp)
                        .background(
                            brush = if (isSelected) colors.tabIndicator else SolidColor(Color.Transparent),
                            shape = RoundedCornerShape(1.dp)
                        )
                )
            }
        }
    }
}

@Composable
private fun FriendListCategoryChips(
    categories: List<String>,
    selectedCategory: String?,
    onCategorySelected: (String?) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        FriendListCategoryChip(
            label = stringResource(R.string.friend_category_all),
            isSelected = selectedCategory == null,
            onClick = { onCategorySelected(null) }
        )
        categories.forEach { category ->
            FriendListCategoryChip(
                label = category,
                isSelected = selectedCategory == category,
                onClick = { onCategorySelected(category) }
            )
        }
    }
}

@Composable
private fun FriendListCategoryChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val colors = LocalAppColors.current.friendList
    Box(
        modifier = Modifier
            .then(
                if (isSelected) Modifier.background(colors.chipSelectedFill, RoundedCornerShape(50))
                else Modifier.border(1.dp, colors.chipUnselectedBorder, RoundedCornerShape(50))
            )
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = label,
            color = if (isSelected) colors.chipSelectedText else colors.chipUnselectedText,
            fontSize = 13.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
private fun FriendListResultsHeader(
    resultCount: Int,
    sortOrder: FriendListSortOrder,
    onSortOrderSelected: (FriendListSortOrder) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalAppColors.current.friendList
    var isSortDialogShowing by remember { mutableStateOf(false) }
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        GradientText(
            text = stringResource(R.string.friend_results_count, resultCount),
            brush = colors.resultsCountText,
            fontSize = 13.sp
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { isSortDialogShowing = true }
        ) {
            Text(
                text = stringResource(
                    when (sortOrder) {
                        FriendListSortOrder.A_TO_Z -> R.string.friend_sort_a_to_z
                        FriendListSortOrder.Z_TO_A -> R.string.friend_sort_z_to_a
                        FriendListSortOrder.RECENTLY_CONTACTED -> R.string.friend_sort_recently_contacted
                    }
                ),
                color = colors.sortText,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                tint = colors.sortText,
                modifier = Modifier.size(18.dp)
            )
        }
    }

    if (isSortDialogShowing) {
        FriendSortDialog(
            currentSortOrder = sortOrder,
            onDismiss = { isSortDialogShowing = false },
            onConfirm = onSortOrderSelected
        )
    }
}

private val previewFriends = listOf(
    FriendResponse(
        id = "F001",
        name = "Angela Reyes",
        nickName = "areyes",
        contactType = ContactType.FACEBOOK,
        avatarUrl = "",
        isFavorite = false,
        tagLabel = "Family"
    ),
    FriendResponse(
        id = "F002",
        name = "Carlos Garcia",
        nickName = "cgarcia",
        contactType = ContactType.PHONE_NUM,
        avatarUrl = "",
        isFavorite = false,
        tagLabel = null
    ),
    FriendResponse(
        id = "F003",
        name = "Isabella Torres",
        nickName = "isabella_t",
        contactType = ContactType.FACEBOOK,
        avatarUrl = "",
        isFavorite = true,
        tagLabel = "Family"
    ),
    FriendResponse(
        id = "F004",
        name = "John Cruz",
        nickName = "jcruz",
        contactType = ContactType.PHONE_NUM,
        avatarUrl = "",
        isFavorite = true,
        tagLabel = "Besties"
    ),
    FriendResponse(
        id = "F005",
        name = "Maria Santos",
        nickName = "marias",
        contactType = ContactType.FACEBOOK,
        avatarUrl = "",
        isFavorite = false,
        tagLabel = null
    ),
    FriendResponse(
        id = "F006",
        name = "Miguel Ramos",
        nickName = "miguelr",
        contactType = ContactType.PHONE_NUM,
        avatarUrl = "",
        isFavorite = false,
        tagLabel = null
    ),
)

private val previewUiState = FriendListUiState(isLoadingFriends = false, friends = previewFriends)

@Preview(name = "Neon", showBackground = true, backgroundColor = 0xFF030F1B)
@Composable
private fun FriendListScreenPreviewNeon() {
    AppTheme(colors = NeonColors) {
        FriendListContent(
            uiState = previewUiState,
            paddingValues = PaddingValues(),
            onBack = {},
            onAddFriendClick = {},
            onSearchQueryChange = {},
            onTabSelected = {},
            onCategorySelected = {},
            onSortOrderSelected = {},
            onToggleFavorite = {},
            onRemoveFriend = {}
        )
    }
}

@Preview(name = "Black Gold", showBackground = true, backgroundColor = 0xFF050505)
@Composable
private fun FriendListScreenPreviewBlackGold() {
    AppTheme(colors = BlackGoldColors, assets = BlackGoldAssets) {
        FriendListContent(
            uiState = previewUiState,
            paddingValues = PaddingValues(),
            onBack = {},
            onAddFriendClick = {},
            onSearchQueryChange = {},
            onTabSelected = {},
            onCategorySelected = {},
            onSortOrderSelected = {},
            onToggleFavorite = {},
            onRemoveFriend = {}
        )
    }
}
