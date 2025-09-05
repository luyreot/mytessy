package com.teoryul.mytesy.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.teoryul.mytesy.domain.appliance.ApplianceEntity
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    stateHolderKey: String,
    viewModel: HomeViewModel = koinViewModel(),
    onAddApplianceClick: () -> Unit,
    onApplianceClick: (ApplianceEntity) -> Unit
) {
    val appliances = viewModel.appliances.collectAsState()
    val applianceUiFlags by viewModel.applianceUiFlags.collectAsState()
    val refreshing by viewModel.isRefreshing.collectAsState()

    PullToRefreshBox(
        isRefreshing = refreshing,
        onRefresh = { viewModel.onPullToRefresh() },
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            if (appliances.value.isEmpty()) {
                EmptyAppliancePlaceholder(
                    onAddApplianceClick = onAddApplianceClick
                )
            } else {
                ApplianceList(
                    items = appliances.value,
                    applianceUiFlags = applianceUiFlags,
                    onApplianceClick = onApplianceClick,
                    onPowerToggle = viewModel::onAppliancePowerToggle
                )
            }
        }
    }
}