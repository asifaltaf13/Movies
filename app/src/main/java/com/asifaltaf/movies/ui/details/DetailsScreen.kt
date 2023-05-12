package com.asifaltaf.movies.ui.details

import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.asifaltaf.movies.R
import com.asifaltaf.movies.ui.theme.MoviesTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun DetailsScreen(
    viewModel: DetailsViewModel = hiltViewModel()
) {
    val detailState by viewModel.detailState.collectAsState()
    val movieDetail = detailState.movieDetail

    val context = LocalContext.current

    val toastMessage by viewModel.toastMessage.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    val scrollState = rememberScrollState()

    val systemUiController = rememberSystemUiController()
    systemUiController.setSystemBarsColor(MaterialTheme.colorScheme.primaryContainer)


    Card(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .animateContentSize(tween()),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        shape = RectangleShape
    ) {
        AsyncImage(
            modifier = Modifier.fillMaxSize(),
            model = ImageRequest.Builder(context = LocalContext.current)
                .data(movieDetail.poster)
                .crossfade(true)
                .build(),
            contentDescription = stringResource(id = R.string.poster),
            contentScale = ContentScale.Crop
        )

        Column(modifier = Modifier.padding(horizontal = 16.dp)) {

            TitleProperty(movieDetail.title)

            DetailProperty("Type", movieDetail.type)

            DetailProperty("Year", movieDetail.year)


            if (isLoading) {
                ShowLoading()
            } else {
                DetailProperty("IMDb Rating", movieDetail.imdbRating)
                DetailProperty("Genre", movieDetail.genre)
                DetailProperty("Country", movieDetail.country)
                DetailProperty("Language", movieDetail.language)
                DetailProperty("Rated", movieDetail.rated)
                DetailProperty("Release date", movieDetail.released)
                DetailProperty("Actor(s)", movieDetail.actors)
                DetailProperty("Director(s)", movieDetail.director)
                DetailProperty("Production", movieDetail.production)
                DetailProperty("Writer(s)", movieDetail.writer)
                DetailProperty("Box Office", movieDetail.boxOffice)
                DetailProperty("Award(s)", movieDetail.awards)
                DetailProperty("IMDb votes", movieDetail.imdbVotes)
                DetailProperty("Metascore", movieDetail.metascore)
                DetailProperty("Runtime", movieDetail.runtime)
                DetailProperty("DVD", movieDetail.dVD)
                DetailProperty("Website", movieDetail.website)
                var ratings = ""
                for (rating in movieDetail.ratings) {
                    ratings += "${rating.value} by ${rating.source}\n"
                }
                DetailProperty("Ratings", ratings.substringBeforeLast("\n"))
                DetailProperty("Plot", movieDetail.plot)

                Spacer(modifier = Modifier.padding(bottom = 24.dp))
            }
        }
    }

    if (!toastMessage.isNullOrEmpty()) {
        Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show()
        viewModel.resetToastMessage()
    }
}


@Composable
fun TitleProperty(title: String) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.padding(bottom = 24.dp))
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
        modifier = Modifier.fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.padding(bottom = 24.dp))
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
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}

@Composable
fun ShowLoading() {
    Box(modifier = Modifier.fillMaxWidth(),) {
        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
    }
}

@Preview
@Composable
fun DetailsScreenPreview() {
    MoviesTheme {
        DetailsScreen()
    }
}