package com.lozanov.viridum.shared

import android.os.Parcelable
import androidx.annotation.StringRes

sealed class NavDestination(val route: String,
                            @StringRes val resourceId: Int) : Parcelable {
    // TODO: Add destinations (and maybe try to add bottomnav navGraph sealed class as nested sealed class here w/ own destinations?)

    @Parcelize
    object Onboarding : NavDestination("onboarding", R.string.onboarding)

    @Parcelize
    object Login : NavDestination("login", R.string.login)

    @Parcelize
    object ARScreen : NavDestination("ar_screen", R.string.ar_screen)

    @Parcelize
    object ModelSelection : NavDestination("model_selection", R.string.model_selection)
}
