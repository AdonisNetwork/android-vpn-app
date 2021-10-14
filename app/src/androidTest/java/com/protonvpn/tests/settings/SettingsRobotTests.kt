/*
 * Copyright (c) 2018 Proton Technologies AG
 *
 * This file is part of ProtonVPN.
 *
 * ProtonVPN is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ProtonVPN is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ProtonVPN.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.protonvpn.tests.settings

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.protonvpn.actions.SettingsRobot
import com.protonvpn.base.BaseVerify
import com.protonvpn.test.shared.TestUser
import com.protonvpn.tests.testRules.ProtonSettingsActivityTestRule
import com.protonvpn.tests.testRules.SetUserPreferencesRule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import org.junit.runner.RunWith

/**
 * [SettingsRobotTests] Contains UI tests for Settings
 */
@LargeTest
@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class SettingsRobotTests {

    private val settingsRobot = SettingsRobot()

    @get:Rule
    var rules = RuleChain
            .outerRule(HiltAndroidRule(this))
            .around(SetUserPreferencesRule(TestUser.getPlusUser()))
            .around(ProtonSettingsActivityTestRule())

    @Test
    fun checkIfSettingsViewIsVisible() {
        settingsRobot.verify {
            mainSettingsAreDisplayed()
        }
    }

    @Test
    fun selectFastestQuickConnection() {
        settingsRobot.setFastestQuickConnection()
                .verify {
                    quickConnectFastestProfileIsVisible()
                }
    }

    @Test
    fun setTooLowMTU() {
        settingsRobot.openMtuSettings()
                .setMTU(1279)
                .clickOnSaveMenuButton()
                .verify {
                    settingsMtuErrorIsShown()
                }
    }

    @Test
    fun setTooHighMTU() {
        settingsRobot.openMtuSettings()
                .setMTU(1501)
                .clickOnSaveMenuButton()
                .verify {
                    settingsMtuErrorIsShown()
                }
    }

    @Test
    fun switchSplitTunneling() {
        settingsRobot.toggleSplitTunneling()
                .verify {
                    splitTunnelIPIsVisible()
                }
        settingsRobot.toggleSplitTunneling()
                .verify {
                    splitTunnelIpIsNotVisible()
                }
    }
}