# CardsScreen

## 結構總覽

```
CardsScreen（有 ViewModel）
  └── CardsScreenContent（無狀態，只接收參數）
        ├── 標題「Cards Management」（永遠顯示）
        └── PullToRefreshBox（weight = 1f）
              ├── Loading  → 空內容（LoadingDialog 另外顯示）
              ├── Error    → 錯誤文字置中
              └── Success
                    ├── cards 為空 → CardsEmptyState
                    │     ├── HorizontalPager → PromoBannerCard × N
                    │     ├── 分頁指示點
                    │     ├── Link Your First Card 按鈕
                    │     └── 安全說明文字
                    └── cards 非空 → LazyColumn
                          ├── CreditCardItem × N
                          └── AddNewCardButton
```

---

## Composable 說明

### `CardsScreen`
**有 ViewModel 的入口層**，負責：
- 訂閱 `viewModel.cardsState`（透過 `collectAsState()`）
- `LaunchedEffect(Unit)` 觸發 `viewModel.fetchCards()`
- 傳入 `onRefresh`、`onNavigate` 給 `CardsScreenContent`

> 注意：`fetchCards()` 由 Composable 端的 `LaunchedEffect` 觸發（而非 ViewModel `init`），因此每次切換回 Cards tab 都會重新發出 API 請求並顯示 Loading。

---

### `CardsScreenContent`
**無狀態的純 UI 層**，可直接用於 `@Preview`。

| 參數 | 說明 |
|------|------|
| `state: CardsState` | 目前的資料狀態 |
| `onRefresh: () -> Unit` | 下拉刷新回呼 |
| `onNavigate: (String) -> Unit` | 導航回呼，傳至子元件 |

標題固定在 `PullToRefreshBox` 上方，不受 state 影響，進入頁面即顯示。

---

## 子元件說明

### `CreditCardItem`
顯示單張信用卡資訊：
- **第一張**（`isPrimary = true`）：`neonPurpleLight` 光暈 + 邊框 + `Primary` badge
- **其他卡**：`neonCyanLight` 光暈 + 邊框
- 卡號自動遮罩：取最後 4 碼，顯示為 `**** **** **** XXXX`
- 卡類型 logo：`visa` → `VISA`、`master` → `MC`、其他 → `CARD`（白底文字）
- 右下角 `Manage` 按鈕（`tokenOrange` 色，`onClick` 待接）

### `CardsEmptyState`
無卡片時的引導畫面：
- `HorizontalPager` 輪播 `PromoBannerCard`（目前 4 頁，僅第 0 頁有內容）
- 輪播加上 `pageSpacing = 16.dp` 顯示相鄰卡片邊緣
- 分頁指示點：當前頁 `8dp neonCyan`，其他 `6dp white 30%`
- `Link Your First Card` 按鈕 → `onNavigate(Routes.SELECT_CARD_TYPE)`
- 底部安全說明文字

### `PromoBannerCard`
輪播中的單頁 Banner：
- 外層 `Box` 加 `padding(horizontal = 12.dp, vertical = 12.dp)` 為 `neonGlow` 光暈保留空間（避免被裁切）
- 內層 `Box`：漸層背景（深藍紫）+ `Brush.linearGradient` 邊框（`neonCyan → neonPurple → neonCyan`）
- 第 0 頁顯示 `VoucherTicket` + 促銷文案，其餘頁面留空

### `VoucherTicket`
用 `drawWithContent` 手繪的票券形狀：
- `Path` 繪製四角圓角矩形，左右中間各有一個半圓缺口
- 背景色 `neonPurple.copy(alpha = 0.12f)` + 描邊 `neonPurple`
- 中央顯示金額文字（如 `₱50`）

### `AddNewCardButton`
- `neonCyanLight` 光暈 + 邊框，膠囊形
- 點擊 → `onNavigate(Routes.SELECT_CARD_TYPE)`

### `InfoCard`（保留但目前未使用）
- 在 `LazyColumn` 中被註解掉，保留供之後啟用

---

## CardsState 狀態機

```
Loading（初始 / fetchCards 中）
  ↓ API 成功                    ↓ API 失敗
Success(cards, isRefreshing)    Error(message)
  ↓ 下拉刷新
  isRefreshing = true → API 完成 → Success(isRefreshing = false)
```

`isRefreshing` 只在已有 `Success` 狀態下的下拉刷新時為 `true`，不會顯示 `LoadingDialog`，改由 `PullToRefreshBox` 的指示器呈現。

---

## 資料流向

```
用戶操作
  → CardsScreen（LaunchedEffect → fetchCards）
  → CardsViewModel.fetchCards() / refreshCards()
  → CardRepository.fetchCreditCardList()
  → CardsState（StateFlow）
  → CardsScreen.collectAsState()
  → 傳入 CardsScreenContent(state = ...) → UI 更新
```

---

## 檔案結構

```
ui/cards/
├── CardsScreen.kt      # 頁面入口 + 所有子元件
└── CardsViewModel.kt   # CardsState / CardsViewModel
```

---

## 注意事項

- 每次切換回 Cards tab 都會重新呼叫 `fetchCards()` 並顯示 `LoadingDialog`，因為 `LaunchedEffect(Unit)` 在 `MainScreen` 的 `AnimatedContent` 切換時會重新執行
- `CardsState` 使用 sealed class 狀態機，與 `HomeUiState`、`ProfileUiState` 的純資料容器設計不同
- `PromoBannerCard` 的光暈保留空間由外層 `Box` 的 `padding` 負責，而非依賴父容器 `overflow`
- `CreditCardItem` 與 `AddNewCardButton` 的 `onNavigate` 目前接至 `Routes.SELECT_CARD_TYPE`（待建立）
- `tokenOrange`（`0xFFFF8C00`）只在此頁使用，已定義為 `private val`
