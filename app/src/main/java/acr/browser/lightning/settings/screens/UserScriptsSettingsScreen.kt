package acr.browser.lightning.settings.screens

import acr.browser.lightning.R
import acr.browser.lightning.preference.UserScriptsPreferenceManager
import acr.browser.lightning.resources.ResourceProvider
import acr.browser.lightning.settings.SettingsBottomSheetInputState
import acr.browser.lightning.settings.SettingsSnackBarState
import acr.browser.lightning.settings.framework.ClickableOnClick
import acr.browser.lightning.settings.framework.ClickableState
import acr.browser.lightning.settings.framework.SettingsFrameworkPresenter
import acr.browser.lightning.settings.framework.SettingsFrameworkScreen
import acr.browser.lightning.settings.framework.SettingsFrameworkState
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import javax.inject.Inject

class UserScriptsSettingsScreen @Inject constructor(
    private val resourceProvider: ResourceProvider,
    private val userScriptsPreferenceManager: UserScriptsPreferenceManager
) {
    val key = "user_scripts"

    fun createSettingsFrameworkState(): SettingsFrameworkState = SettingsFrameworkState(
        title = resourceProvider.stringResource(R.string.settings_user_scripts),
        content = listOf(
            ClickableState(
                title = resourceProvider.stringResource(R.string.setting_custom_css),
                summary = {
                    val css = userScriptsPreferenceManager.getCustomCss()
                    if (css.isBlank()) {
                        resourceProvider.stringResource(R.string.setting_user_script_empty)
                    } else {
                        css.take(60) + if (css.length > 60) "…" else ""
                    }
                },
                onClick = ClickableOnClick.Input(
                    produceState = {
                        SettingsBottomSheetInputState(
                            title = resourceProvider.stringResource(R.string.setting_custom_css),
                            hint = "body { background: #fff; }",
                            currentValue = userScriptsPreferenceManager.getCustomCss()
                        )
                    },
                    onValueUpdated = { value ->
                        userScriptsPreferenceManager.setCustomCss(value)
                        ClickableOnClick.Snackbar {
                            SettingsSnackBarState(
                                message = resourceProvider.stringResource(R.string.setting_user_script_saved)
                            )
                        }
                    }
                )
            ),
            ClickableState(
                title = resourceProvider.stringResource(R.string.setting_custom_js),
                summary = {
                    val js = userScriptsPreferenceManager.getCustomJs()
                    if (js.isBlank()) {
                        resourceProvider.stringResource(R.string.setting_user_script_empty)
                    } else {
                        js.take(60) + if (js.length > 60) "…" else ""
                    }
                },
                onClick = ClickableOnClick.Input(
                    produceState = {
                        SettingsBottomSheetInputState(
                            title = resourceProvider.stringResource(R.string.setting_custom_js),
                            hint = "document.title = 'Hello, Nexus!';",
                            currentValue = userScriptsPreferenceManager.getCustomJs()
                        )
                    },
                    onValueUpdated = { value ->
                        userScriptsPreferenceManager.setCustomJs(value)
                        ClickableOnClick.Snackbar {
                            SettingsSnackBarState(
                                message = resourceProvider.stringResource(R.string.setting_user_script_saved)
                            )
                        }
                    }
                )
            )
        )
    )
}

@Composable
fun UserScriptsSettingsScreen(
    userScriptsSettingsScreen: UserScriptsSettingsScreen,
    onUp: () -> Unit
) {
    SettingsFrameworkScreen(
        viewModel(
            key = userScriptsSettingsScreen.key,
            factory = SettingsFrameworkPresenter.Factory(
                settingsFrameworkState = {
                    userScriptsSettingsScreen.createSettingsFrameworkState()
                }
            )
        ),
        onUp
    )
}
