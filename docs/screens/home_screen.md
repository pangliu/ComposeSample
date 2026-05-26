# HomeScreen 頁面結構說明

## 架構概覽

HomeScreen 是 **Home Tab 的純內容頁面**，由 `MainScreen` 透過 tab 切換呼叫，不含任何導覽邏輯。

```
MainScreen（導覽容器）
  └── HomeScreen()          ← 自己注入 HomeViewModel，管理自己的所有資料
        └── HomeViewModel
              ├── uiState: HomeUiState              ← 統一 UI 狀態（所有初始 API 的結果）
              ├── eventFlow: SharedFlow<UiEvent>    ← 一次性事件（Toast 等）
              └── myMenuItems: List<EssentialItem>  ← My Menu 持久化（SharedPreferences）
```

---

## 檔案對應

| 檔案 | 說明 |
|------|------|
| `ui/home/HomeScreen.kt` | Home 頁入口 + 內容 composable |
| `ui/home/HomeViewModel.kt` | UI 狀態管理、API 呼叫、My Menu 持久化 |
| `ui/home/balance/BalanceCard.kt` | 資產卡片 |
| `ui/home/essential/XEssentialsCard.kt` | X-Essentials 快捷功能區 |
| `ui/home/essential/EssentialItems.kt` | `EssentialItem` 資料模型 + 全部功能項目清單 |
| `ui/home/essential/EditEssentialsDialog.kt` | 拖曳排序 Dialog |
| `ui/home/quests/QuestCard.kt` | Quest 任務卡片（開發中） |
| `ui/home/recent/RecentActivity.kt` | 最近活動（串接 `/api/order/history`） |
| `network/model/response/OrderHistoryResponse.kt` | 交易紀錄 response model |

---

## Composable 層級樹

```
HomeScreen()                        ← hiltViewModel() 注入 HomeViewModel
├── LoadingDialog(isShowing = uiState.isLoading)   ← 所有初始 API 完成前共用
└── HomeScreenContent(uiState, myMenuItems, onSaveMyMenu)
    └── Box (fillMaxSize)
        └── Column
              ├── HeaderSection                         ← 固定，不隨滾動移動
              └── Column (weight(1f) + verticalScroll)  ← 可滾動區域
                    ├── BalanceCard
                    ├── XEssentialsCard
                    ├── QuestCard
                    └── RecentActivity
```

---

## HomeUiState（統一 UI 狀態）

`HomeViewModel` 以單一 `HomeUiState` 管理所有初始 API 的結果，是**純資料容器**，不使用 sealed class 狀態機：

```kotlin
data class HomeUiState(
    val isLoadingUserInfo: Boolean = true,
    val isLoadingOrders: Boolean = true,
    val userInfo: UserInfoResponse = UserInfoResponse.empty(),
    val orders: List<OrderHistoryResponse> = emptyList(),
) {
    /** 任一 API 仍在載入中即為 true */
    val isLoading: Boolean get() = isLoadingUserInfo || isLoadingOrders
}
```

- 每隻 API 對應一個 `isLoadingX: Boolean = true` 欄位；`isLoading` 計算屬性 = 所有旗標的 OR
- 資料缺失使用空預設值（`UserInfoResponse.empty()`、`emptyList()`），不進入錯誤 UI 狀態
- API 錯誤透過 `eventFlow` 發送 Toast，不修改 UiState 為錯誤狀態

---

## Home Tab 內容佈局

```
HomeScreenContent
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
        └── Spacer(20dp)
```

---

## 各區塊說明

### A. HeaderSection（固定頂部）

```
Row (fillMaxWidth, padding top=24dp bottom=16dp, SpaceBetween)
├── Row → 問候文字（stringResource home_greeting + userName, 20.sp Bold）
└── Row → 右側圖示
    ├── Icon (size=24dp) + clickable → 通知 (Notifications icon)
    └── Icon (size=24dp) + clickable → 設定 (Settings icon)
```

**注意**：右側圖示使用 `Icon + clickable(indication = null)`，而非 `IconButton`。
`IconButton` 預設最小觸控高度 48dp 會撐高整個 Row，導致標題高度與其他 Tab 頁面不一致。

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
| Cash In | `CashInGreen` | TODO |
| Send | `SendPink` | TODO |
| Balance Switch | `BalanceSwitchBackground` | TODO |

---

### C. XEssentialsCard（X-Essentials 快捷功能區）

**位置**：`ui/home/essential/XEssentialsCard.kt`  
**參數**：`myMenuItems: List<EssentialItem>`, `onSaveMyMenu: (List<EssentialItem>) -> Unit`

```
Column
├── Row → 標題列（"X-Essentials" + [More] + [Edit]）
└── Box (漸層背景卡片)
    ├── HorizontalPager（每頁 8 個，2 排 × 4 欄）
    └── Row → 圓點頁碼指示器
└── [條件渲染] EditEssentialsDialog
```

**持久化**：僅存 `label`（`EssentialsManager`），讀取時從 `allEssentialItems` 反查（`ImageVector` 無法序列化）。

---

### D. QuestCard（任務卡片）

**位置**：`ui/home/quests/QuestCard.kt`  
> 狀態：**開發中（placeholder）**

```
Box
├── Text → 標題
├── Row (height 120dp) → NeonPurple 邊框卡片主體
└── Image → bg_quest_card（右下角裝飾圖，offset x=3dp）
```

---

### E. RecentActivity（最近活動）

**位置**：`ui/home/recent/RecentActivity.kt`  
**參數**：`orders: List<OrderHistoryResponse>`  
**API**：`GET /api/order/history`（需 Token，由 `AuthInterceptor` 自動附加）

```
Column
├── Text → "Recent Activity"
└── Box (NeonCyanLight 邊框)
    ├── orders.isEmpty()    → "No recent transactions"
    └── orders.isNotEmpty() → Column
          └── TransactionRow × N（icon + 說明 + 金額 + 箭頭）
```

**TransactionRow 邏輯**：
- `OrderType.INCOMING` → `ArrowDownward` + `CashInGreen` + `+{amount}`
- `OrderType.OUTGOING` → `ArrowUpward` + `SendPink` + `-{amount}`

---

## ViewModel 資料流

```
HomeViewModel.init()
  ├── fetchUserInfo()       ← 平行呼叫
  └── fetchOrderHistory()   ← 平行呼叫

每個 fetch 完成時透過 _uiState.update { it.copy(...) } 更新對應欄位：

fetchUserInfo()
  ├── Success   → _uiState.update { it.copy(isLoadingUserInfo = false, userInfo = data) }
  ├── Error     → _uiState.update { it.copy(isLoadingUserInfo = false) }
  │               + _eventFlow.emit(UiEvent.ShowToast(message))
  └── Exception → _uiState.update { it.copy(isLoadingUserInfo = false) }
                  + _eventFlow.emit(UiEvent.ShowToast(...))

fetchOrderHistory()
  ├── Success   → _uiState.update { it.copy(isLoadingOrders = false, orders = data) }
  ├── Error     → _uiState.update { it.copy(isLoadingOrders = false) }
  │               + _eventFlow.emit(UiEvent.ShowToast(message))
  └── Exception → _uiState.update { it.copy(isLoadingOrders = false) }
                  + _eventFlow.emit(UiEvent.ShowToast(...))

saveMyMenu(items)
  ├── _myMenuItems.value = items
  └── essentialsManager.save(items)
```

**Toast 收集**：Composable 以 `LaunchedEffect(Unit)` 收集 `eventFlow`，收到 `UiEvent.ShowToast` 時呼叫 `Toast.makeText()`。

---

## 新增初始 API 步驟

HomeScreen 進入時需要呼叫的 API 統一在 `HomeViewModel` 管理，新增步驟如下：

### Step 1 — 建立 Response Model

在 `network/model/response/` 新增 data class，使用 `@JsonClass(generateAdapter = true)` 與 `@Json(name = "...")` 對應 snake_case：

```kotlin
@JsonClass(generateAdapter = true)
data class NewDataResponse(
    @Json(name = "field_name") val fieldName: String
)
```

### Step 2 — 在 `UserApiService` 新增 endpoint

需要 Token 的 API 放在 `UserApiService`（`@AuthClient`）：

```kotlin
@GET("/api/new/endpoint")
suspend fun getNewData(): BaseResponse<NewDataResponse>
```

### Step 3 — 在 `FakeUserApiService` 加入假資料

```kotlin
override suspend fun getNewData(): BaseResponse<NewDataResponse> {
    delay(800)
    return BaseResponse(code = 200, errorMsg = "成功", result = NewDataResponse(...))
}
```

### Step 4 — 在 `UserRepository` 新增 method

```kotlin
suspend fun fetchNewData(): NetworkResult<NewDataResponse> {
    return safeApiCall { apiService.getNewData() }
}
```

### Step 5 — 在 `HomeUiState` 新增欄位

在 `HomeUiState` 加入一個 boolean loading 旗標與對應資料欄位，`isLoading` 計算屬性自動包含新旗標：

```kotlin
data class HomeUiState(
    val isLoadingUserInfo: Boolean = true,
    val isLoadingOrders: Boolean = true,
    val isLoadingNewData: Boolean = true,       // ← 新增
    val userInfo: UserInfoResponse = UserInfoResponse.empty(),
    val orders: List<OrderHistoryResponse> = emptyList(),
    val newData: NewDataResponse? = null,       // ← 新增
) {
    val isLoading: Boolean
        get() = isLoadingUserInfo || isLoadingOrders || isLoadingNewData  // ← 自動包含
}
```

### Step 6 — 在 `HomeViewModel.init()` 呼叫並更新 state

```kotlin
init {
    fetchUserInfo()
    fetchOrderHistory()
    fetchNewData()    // ← 新增
}

private fun fetchNewData() {
    viewModelScope.launch {
        when (val result = userRepository.fetchNewData()) {
            is NetworkResult.Success -> _uiState.update {
                it.copy(isLoadingNewData = false, newData = result.data)
            }
            is NetworkResult.Error -> {
                _uiState.update { it.copy(isLoadingNewData = false) }
                _eventFlow.emit(UiEvent.ShowToast(result.message))
            }
            is NetworkResult.Exception -> {
                _uiState.update { it.copy(isLoadingNewData = false) }
                _eventFlow.emit(UiEvent.ShowToast(result.e.message ?: "網路異常"))
            }
        }
    }
}
```

### Step 7 — 在 `HomeScreenContent` 使用資料

```kotlin
NewDataWidget(data = uiState.newData)
```

> **LoadingDialog 自動共用**：只要 `isLoadingNewData` 預設為 `true` 且加入 `isLoading` 的 OR 運算，進入頁面時就會自動等待新 API 完成後才顯示內容，不需要其他改動。

---

## 開發指引

| 要做的事 | 參考位置 |
|---------|---------|
| 新增初始 API | 見上方「新增初始 API 步驟」 |
| 新增快捷功能項目 | `EssentialItems.kt` → `allEssentialItems` |
| 實作 Cash In / Send 功能 | `BalanceCard.kt` → TODO 區塊 |
| 實作 Quest Card 內容 | `QuestCard.kt` → Row 內的 wording |
| 新增顯示字串 | `res/values/strings.xml`（禁止在 Composable 中硬編碼） |
| 新增底部 Tab | 見 `main_screen.md` |
