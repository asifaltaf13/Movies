package com.asifaltaf.movies.ui.home.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.asifaltaf.movies.R
import com.asifaltaf.movies.ui.theme.Phosphate

@Composable
fun NoResults() {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val rotateAnimationDuration = 2*60*1000
            val infiniteTransition = rememberInfiniteTransition()
            val rotateAnimationDegrees by infiniteTransition.animateFloat(
                initialValue = 0f,
                targetValue = 360f,
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        durationMillis = rotateAnimationDuration,
                        easing = LinearEasing
                    )
                )
            )

            Image(
                modifier = Modifier
                    .rotate(degrees = rotateAnimationDegrees),
                painter = painterResource(id = R.drawable.no_results),
                contentDescription = stringResource(R.string.no_results)
            )
            Spacer(modifier = Modifier.padding(16.dp))
            Text(
                text = "Nothing to show",
                fontFamily = Phosphate,
                color = MaterialTheme.colorScheme.onTertiaryContainer
            )
        }
    }
}

@Preview
@Composable
fun NoResultPreview() {
    NoResults()
}