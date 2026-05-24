package com.example.task.domain

import android.content.Context
import android.util.Log
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.state.GlanceStateDefinition
import androidx.glance.state.PreferencesGlanceStateDefinition
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextAlign
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.example.task.MainActivity
import com.example.task.R
import com.example.test.Accent
import com.example.test.AccentInactive

class GlanceWidgetReceiver: GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = GlanceWidget()
}

object WidgetStateKeys {
    val token = stringPreferencesKey("token")
    val projectName = stringPreferencesKey("project_name")
    val projectData = stringPreferencesKey("project_days_past")
}

class GlanceWidget: GlanceAppWidget() {

    override val stateDefinition: GlanceStateDefinition<*> = PreferencesGlanceStateDefinition

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            val userPrefs = currentState<Preferences>()
            val token = userPrefs[WidgetStateKeys.token]
            val projectName = userPrefs[WidgetStateKeys.projectName] ?: "Выберите проект"
            val projectData = userPrefs[WidgetStateKeys.projectData] ?: ""

            Column(
                modifier = GlanceModifier
                    .fillMaxSize()
                    .background(AccentInactive)
                    .padding(16.dp)
                    .clickable (
                        actionStartActivity<MainActivity>()
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (!token.isNullOrEmpty()) {
                    Text(text = projectName.ifEmpty { "Выберите проект" },
                        style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold, color = ColorProvider(
                            Accent),textAlign = TextAlign.Center))
                    Spacer(GlanceModifier.height(10.dp))
                    Text(text = projectData.ifEmpty { "" },
                        style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold, color = ColorProvider(
                            Accent),textAlign = TextAlign.Center))
                } else {
                    Text(text = "Войдите",
                        style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold, color = ColorProvider(
                            Accent),textAlign = TextAlign.Center))
                }
            }
        }
    }
}