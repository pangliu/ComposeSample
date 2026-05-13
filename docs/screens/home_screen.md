# HomeScreen 頁面結構說明

## 架構概覽

HomeScreen 採用 **MVVM + Repository** 架構，資料流如下：

```
HomeScreen (Composable)
  └── HomeViewModel (StateFlow)
        ├── homeState: HomeState       ← API 資料（使用者資訊）
        └── myMenuItems: List<EssentialItem>  ← My Menu 排序（SharedPreferences）
              └── UserRepository → FakeUserApiService（目前為假資料）
```

---

## 檔案對應

| 檔案 | 說明 |
|------|------|
| `ui/home/HomeScreen.kt` | 主畫面入口、Tab 切換、HomeTab 內容 |
| `ui/home/HomeViewModel.kt` | 狀態管理、API 呼叫、My Menu 持久化 |
| `ui/home/balance/BalanceCard.kt` | 資產卡片 |
| `ui/home/essential/XEssentialsCard.kt` | X-Essentials 快捷功能區 |
| `ui/home/essential/EssentialItems.kt` | `EssentialItem` 資料模型 + 全部功能項目清單 |
| `ui/home/essential/EditEssentialsDialog.kt` | 拖曳排序 Dialog |
| `ui/home/quests/QuestCard.kt` | Quest 任務卡片（開發中） |
| `ui/home/recent/RecentActivity.kt` | 最近活動區塊（開發中） |
| `ui/home/nvaTab/CustomBottomNavigation.kt` | 底部導覽列 |

---

## Composable 層級樹

```
HomeScreen(viewModel)
└── HomeScreenContent(state, myMenuItems, onSaveMyMenu)
    └── Scaffold
        ├── bottomBar: CustomBottomNavigation       ← 固定底部，高度 60dp
        └── content: AnimatedContent (tab 切換動畫)
            ├── index=0 → HomeTabContent            ← 本文件主要描述的內容
            ├── index=1 → CardsScreen
            ├── index=2 → QuestsScreen
            └── index=3 → ProfileScreen
```

---

## HomeState 狀態機

`HomeViewModel` 以 `HomeState` 管理畫面狀態：

```kotlin
sealed class HomeState {
    object Loading : HomeState()
    data class Success(val userInfo: UserInfoResponse) : HomeState()
    data class Error(val message: String) : HomeState()
}
```

- **Loading**：顯示 `LoadingDialogContent()`（全屏 loading 遮罩）
- **Error**：顯示錯誤訊息文字（置中）
- **Success**：顯示完整的 Home 內容（HeaderSection + 可滾動區域）

---

## Home Tab 內容佈局（Success 狀態）

```
HomeTabContent
└── Column (fillMaxSize, padding horizontal 16dp)
    ├── HeaderSection          ← 固定，不隨滾動移動
    └── Column                 ← weight(1f) + verticalScroll，可滾動
        ├── Spacer(16dp)
        ├── BalanceCard
        ├── Spacer(10dp)
        ├── XEssentialsCard
        ├── Spacer(10dp)
        ├── QuestCard
        ├── Spacer(10dp)
        ├── RecentActivity
        └── Spacer(20dp)       ← 底部留白，避免內容被 nav bar 遮住
```

---

## 各區塊說明

### A. HeaderSection（固定頂部）

**位置**：`HomeScreen.kt`

```
Row (fillMaxWidth, SpaceBetween)
├── Row → 問候文字（"Hi, {userName}!"）
└── Row → 右側圖示
    ├── IconButton → 通知 (Notifications icon)
    └── IconButton → 設定 (Settings icon)
```

- 文字來源：`stringResource(R.string.home_greeting, userName)`
- 圖示顏色：`Color.Gray`

---

### B. BalanceCard（資產卡片）

**位置**：`ui/home/balance/BalanceCard.kt`

**參數**：`cashBalance: Double`, `tokenBalance: Double`

**內部狀態**：`isBalanceHidden: Boolean`（眼睛圖示切換顯示/隱藏）

```
Box (fillMaxWidth)
├── Image → 背景圖 (bg_balance_card, ContentScale.FillWidth)
└── Column (matchParentSize, SpaceBetween)
    ├── Row 1：BALANCE 標題 + 眼睛 toggle + [Cash In] 綠色按鈕
    ├── Row 2：PHP {cashBalance} 大字 + [Send] 粉紫色按鈕
    └── Row 3：金幣 icon + {tokenBalance} + [Balance Switch] 深色按鈕
```

| 按鈕 | 顏色 | 功能 |
|------|------|------|
| Cash In | `CashInGreen` | TODO: 儲值入帳 |
| Send | `SendPink` | TODO: 轉帳出帳 |
| Balance Switch | `BalanceSwitchBackground` | TODO: 切換餘額類型 |
| 眼睛 icon | `NeonCyan`（啟用）/ `Gray`（關閉） | 切換金額顯示/隱藏 |

---

### C. XEssentialsCard（X-Essentials 快捷功能區）

**位置**：`ui/home/essential/XEssentialsCard.kt`

**參數**：`myMenuItems: List<EssentialItem>`, `onSaveMyMenu: (List<EssentialItem>) -> Unit`

```
Column
├── Row → 標題列
│   ├── Text → "X-Essentials"
│   └── Row
│       ├── [More] 邊框按鈕
│       └── [Edit] 實心按鈕 → 開啟 EditEssentialsDialog
├── Box (漸層背景卡片)
│   ├── HorizontalPager (可左右滑動)
│   │   └── Page (每頁 8 個 items，2 排 x 4 欄)
│   │       ├── Row → 上排 4 個 EssentialItemView
│   │       └── Row → 下排 4 個 EssentialItemView
│   └── Row → 圓點頁碼指示器 (NeonCyan 選中 / 半透明白未選中)
└── [條件渲染] EditEssentialsDialog (showEditDialog = true 時)
```

**EssentialItem 資料結構**：

```kotlin
data class EssentialItem(
    val iconVector: ImageVector? = null,
    val iconRes: Int? = null,         // @DrawableRes mipmap
    val label: String,
    val useOriginalColor: Boolean = false,
    val onClick: () -> Unit = {}
)
```

**數量常數**：
- `ESSENTIALS_DISPLAY_COUNT = 16`：XEssentialsCard 顯示上限
- `ITEMS_PER_PAGE = 8`：每頁數量（2 排 × 4 欄）
- `allEssentialItems`：全部 20 個功能項目（唯一資料來源）

**持久化**：僅存 `label`（`EssentialsManager` via `SharedPreferences`），讀取時從 `allEssentialItems` 反查完整物件（因 `ImageVector` 無法序列化）。

---

### D. QuestCard（任務卡片）

**位置**：`ui/home/quests/QuestCard.kt`

> 狀態：**開發中（placeholder）**

```
Box (fillMaxWidth)
├── Text → "Quest Card" 標題
├── Row (align BottomEnd, height 120dp)  ← 紫色邊框卡片主體
│   └── Text → "wording here"（佔位文字）
└── Image → bg_quest_card (size 150dp, align BottomEnd)  ← 右下角人物裝飾圖
```

- 卡片邊框：`NeonPurple` neonGlow 效果
- 裝飾圖略微向右 offset(x = 3dp)，讓圖片超出卡片右側

---

### E. RecentActivity（最近活動）

**位置**：`ui/home/recent/RecentActivity.kt`

> 狀態：**開發中（placeholder）**

```
Column (fillMaxWidth)
├── Text → "Recent Activity" 標題
├── Spacer(10dp)
└── Box (fillMaxWidth, height 120dp)  ← NeonCyanLight 邊框容器（內容待實作）
```

---

## 底部導覽列（CustomBottomNavigation）

**位置**：`ui/home/nvaTab/CustomBottomNavigation.kt`

**高度**：固定 60dp（`NavBarHeight`）

```
Box (fillMaxWidth, height 60dp, contentAlignment = BottomCenter)
├── Row (background WelcomeBackground, SpaceEvenly)  ← 底部背景列
│   ├── BottomNavItem (weight 1f) → Home    (index 0)
│   ├── BottomNavItem (weight 1f) → Cards   (index 1)
│   ├── Spacer         (weight 1f)          ← 中間佔位（給 ScanAndPayTab 用）
│   ├── BottomNavItem (weight 1f) → Quests  (index 2)
│   └── BottomNavItem (weight 1f) → Profile (index 3)
└── ScanAndPayTab()  ← overlay，wrapContentHeight(unbounded=true) 超出 nav bar 高度
```

**Tab 切換動畫**：`AnimatedContent` + `slideInHorizontally`/`slideOutHorizontally`（左右滑動方向根據 index 大小決定）

**ScanAndPayTab 設計重點**：
- `wrapContentHeight(unbounded = true)`：允許 Column 突破父層 60dp 高度限制向上延伸
- `ContentScale.FillWidth`：背景圖維持原始比例，不被裁切
- `padding(bottom = 8.dp)`：與 `BottomNavItem` 相同，確保文字底部對齊

---

## ViewModel 資料流

```
init → fetchData()
  └── userRepository.fetchUserInfo()     (Fake: 模擬延遲 1000ms)
        ├── Success → homeState = HomeState.Success(userInfo)
        ├── Error   → homeState = HomeState.Error(message)
        └── Exception → homeState = HomeState.Error(e.message)

saveMyMenu(items)
  ├── _myMenuItems.value = items         (更新 StateFlow，觸發 UI 重組)
  └── essentialsManager.save(items)     (持久化到 SharedPreferences)
```

---

## 新增內容的開發指引

| 要做的事 | 參考位置 |
|---------|---------|
| 新增快捷功能項目 | `EssentialItems.kt` → `allEssentialItems` |
| 實作 Cash In / Send 功能 | `BalanceCard.kt` → TODO 區塊 |
| 實作 Quest Card 內容 | `QuestCard.kt` → Row 內的 wording |
| 實作 Recent Activity 列表 | `RecentActivity.kt` → Box 內容 |
| 新增第 5 個底部 Tab | `CustomBottomNavigation.kt` + `HomeScreenContent` 的 `when (index)` |
| 新增顯示字串 | `res/values/strings.xml`（禁止在 Composable 中硬編碼） |
