package com.asifaltaf.movies.ui.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.asifaltaf.movies.R
import com.asifaltaf.movies.ui.theme.Phosphate

@Composable
fun DetailsScreen(
    viewModel: DetailsViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val movie = state.movie

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = stringResource(R.string.details),
            fontFamily = Phosphate,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            style = MaterialTheme.typography.headlineMedium
        )

        Surface(
            modifier = Modifier.padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.secondaryContainer,
            shadowElevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 16.dp)
            ) {

                Surface(
                    shape = RoundedCornerShape(16.dp),
                    shadowElevation = 8.dp
                ){
                    AsyncImage(
                        modifier = Modifier.fillMaxSize(),
                        model = ImageRequest.Builder(context = LocalContext.current)
                            .data(movie.posterUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = stringResource(id = R.string.poster),
                        contentScale = ContentScale.Crop,
                        error = painterResource(id = R.drawable.ic_broken_image)
                    )
                }
                
                Spacer(modifier = Modifier.padding(8.dp))

                Surface(
                    modifier = Modifier.padding(),
                    shape = RoundedCornerShape(16.dp),
                    shadowElevation = 8.dp,
                    color = MaterialTheme.colorScheme.primaryContainer
                ){
                    Column(
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 0.dp, bottom = 24.dp),
                    ) {
                        TitleProperty(movie.title)

                        DetailProperty("Type", movie.type)
                        Spacer(modifier = Modifier.padding(bottom = 16.dp))
                        DetailProperty("Year", movie.year)
                    }
                }
            }
        }
    }
}


@Composable
fun TitleProperty(title: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            fontWeight = FontWeight.Bold,
        )
    }
}


@Composable
fun DetailProperty(
    label: String, value: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Divider(color = MaterialTheme.colorScheme.tertiary)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier.padding(end = 8.dp),
                text = label,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.secondary
            )
            Text(
                modifier = Modifier.padding(start = 8.dp),
                text = value,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}