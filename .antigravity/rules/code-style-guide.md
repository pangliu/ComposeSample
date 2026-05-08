# ComposeSample 專案程式碼規範 (Code Style Guide)

## 1. 視覺與設計系統 (UI & Theme)
- **嚴禁硬編碼顏色 (Hardcoded Colors)**：
    - 所有顏色必須從 `com.example.newproject.ui.theme` 中取得（例如 `NeonCyan`, `NeonPurple`, `InputFieldDark`, `DarkOverlay`, `CashInGreen`, `SendPink`）。
    - **色碼定義**：禁止在 UI 程式碼中直接寫入 Hex Code（如 `Color(0xFF...)`），所有新使用的色碼必須先定義於 `ui/theme/Color.kt` 檔案中再行調用。
- **霓虹效果 (Neon Effects)**：
    - 使用 `shadow` 搭配 `spotColor` 來模擬發光效果。
    - 複雜對齊優先使用 `Box` 與 `offset` 而非 `Column` 內的 `Spacer`，以避免陰影壓迫排版。
- **點擊反饋**：`clickable` 在深色背景上若長按出現矩形 ripple 陰影，應加 `indication = null` 處理。

## 2. 資源管理 (Resource Management)
- **字串資源**：
    - 所有 UI 顯示文字必須放置於 `strings.xml`，並透過 `stringResource(R.string...)` 調用。
    - `strings.xml` 應依頁面加上區塊註解（如 `<!-- HomeScreen -->`）。
    - 格式化字串使用 `%s` / `%d` 佔位符。
- **持久化 (Persistence)**：
    - 統一使用 `SharedPreferences` 檔案名稱 `"app_prefs"`。
    - `EssentialsItem` 持久化僅存 `label` 字串，讀取時反查完整物件。

## 3. Compose 開發規範
- **參數格式化**：若 Composable 函式的參數超過 3 個，必須採取「一參數一行」的排列方式。
- **預覽 (Preview)**：
    - 每個 UI 元件必須提供 `@Preview` 函式。
    - 參數應給予預設值（如 `onEvent: () -> Unit = {}`），方便 Preview。
- **動畫與佈局**：
    - 避免在 Composable 內直接使用 `BoxWithConstraints`，改用 `Box + onSizeChanged`。
    - Dialog 動畫優先使用 `AnimatedVisibility` + `slideInVertically/fadeIn`。

## 4. 架構與邏輯規範 (Architecture)
- **MVVM + Repository**：
    - 流程：`Composable → ViewModel (StateFlow) → Repository → API / Manager`。
    - Composable 只讀 StateFlow，不直接呼叫 Repository。
- **依賴注入 (DI)**：
    - ViewModel 使用 `@HiltViewModel` + `@Inject constructor`。
    - 需要 Context 時使用 `@ApplicationContext`。
- **網路請求 (Network)**：
    - API 呼叫統一透過 `safeApiCall {}` 包裝，回傳 `NetworkResult<T>`。
    - ViewModel 必須完整處理 `Success`、`Error`、`Exception` 三種分支。
    - 新增 API 時必須同步在 `Fake*ApiService` 加入模擬實作（含 `delay()`）。
    - 區分 `@PublicClient`（無 Token）與 `@AuthClient`（有 Token）。
- **API 模型**：網路傳輸模型必須使用 Moshi 標記（`@JsonClass(generateAdapter = true)` 與 `@Json(name = "...")`）。
