package com.lozanov.viridum.state

sealed class ARAvailabilityState {
    object NotInstalledAndShouldTakeAction : ARAvailabilityState()

    object ErrorOrTimedOut : ARAvailabilityState()

    object DeviceNotCapable : ARAvailabilityState()

    object Checking : ARAvailabilityState()

    object Installed : ARAvailabilityState()
}
