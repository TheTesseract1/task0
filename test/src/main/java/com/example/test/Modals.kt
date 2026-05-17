package com.example.test

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/* Этот код представляет собой BaseModal — это обёртка (шаблон)
для нижнего всплывающего экрана (Bottom Sheet).
В мобильных приложениях такие экраны используются для фильтров,
выбора параметров или дополнительных меню, которые «выезжают» снизу. */

@OptIn(ExperimentalMaterial3Api::class) //Это разрешение на использование «экспериментального» кода.
// Компонент ModalBottomSheet в библиотеке Material 3 всё еще официально считается экспериментальным
@Composable
fun BaseModal(
    onDismissRequest: () -> Unit, //действие при закрытии (Функция, которая сработает, когда пользователь нажмет «назад», кликнет мимо модалки или смахнет её вниз)
    content: @Composable () -> Unit  //то ui, который будет внутри модалки
) {
    ModalBottomSheet(
        dragHandle = null, //По умолчанию сверху шторки рисуется маленькая серая полоска (индикатор того, что за неё можно тянуть).
        // Здесь она отключена (null), чтобы дизайн был более чистым.
        onDismissRequest = onDismissRequest, //Пробрасываем действие закрытия
        shape = AbsoluteRoundedCornerShape(topLeft = 24.dp, topRight = 24.dp), //Закругляем только верхние углы на 24.dp.
        // Нижние остаются острыми, так как они прилегают к низу экрана
        containerColor = White,  //устанавливаем фон шторки белым.
        sheetState = rememberModalBottomSheetState(
            skipPartiallyExpanded = true //Если false (по умолчанию), шторка может открыться сначала наполовину, а потом её нужно тянуть выше.
                   // Если true, шторка всегда открывается сразу на всю высоту своего контента. Это удобнее для маленьких форм.
        )
    ) {
        Spacer(Modifier.height(24.dp)) // Отступ сверху, чтобы контент не прилипал к скругленным краям
        Column(
            modifier = Modifier.padding(horizontal = 20.dp)  // Боковые отступы для контента
        ) {
            content() // Вызов твоего UI, который ты передал в параметры
        }
    }
}

