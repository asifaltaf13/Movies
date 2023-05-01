package com.asifaltaf.movies.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.asifaltaf.movies.R
import com.asifaltaf.movies.domain.model.MovieEntity
import com.asifaltaf.movies.ui.util.Tags

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieCard(
    modifier: Modifier = Modifier,
    movie: MovieEntity,
    onCardClick: () -> Unit = {}
) {
    Card(
        modifier = modifier
            .testTag(Tags.MovieCard)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = RoundedCornerShape(16.dp),
        onClick = onCardClick
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                modifier = Modifier.fillMaxWidth(),
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(movie.posterUrl)
                    .crossfade(500)
                    .build(),
                contentDescription = stringResource(id = R.string.poster),
                contentScale = ContentScale.Crop,
                alignment = Alignment.TopCenter,
                error = painterResource(id = R.drawable.ic_broken_image)
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.85f))
                    .align(Alignment.BottomStart)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 4.dp, bottom = 8.dp, start = 8.dp, end = 8.dp)
                ) {
                    Text(
                        modifier = Modifier.align(CenterHorizontally),
                        text = movie.title,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }
    }
}
