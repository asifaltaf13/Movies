package com.asifaltaf.movies.ui.home.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Size
import com.asifaltaf.movies.R
import com.asifaltaf.movies.domain.model.MovieEntity
import com.asifaltaf.movies.ui.util.Tags

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieCard(
    modifier: Modifier = Modifier, movie: MovieEntity, onCardClick: () -> Unit = {}
) {
    val scaleAnimation = remember { Animatable(1f) }
    LaunchedEffect(Unit) {
        scaleAnimation.animateTo(
            targetValue = 1.1f, animationSpec = tween(durationMillis = 5_000, easing = LinearEasing)
        )
    }

    Card(
        modifier = modifier
            .testTag(Tags.MovieCard)
            .wrapContentSize(),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = MaterialTheme.shapes.medium,
        onClick = onCardClick
    ) {
        var errorColor by remember { mutableStateOf(false) }
        AsyncImage(modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .background(
                if (errorColor) MaterialTheme.colorScheme.errorContainer
                else MaterialTheme.colorScheme.surface
            )
            .scale(scaleAnimation.value),
            model = ImageRequest.Builder(context = LocalContext.current).data(movie.posterUrl)
                .crossfade(500).size(Size.ORIGINAL).build(),
            contentDescription = stringResource(id = R.string.poster),
            contentScale = ContentScale.Crop,
            alignment = Alignment.TopCenter,
            onError = {
                errorColor = true
            })

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(MaterialTheme.colorScheme.primaryContainer),
            text = movie.title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}
