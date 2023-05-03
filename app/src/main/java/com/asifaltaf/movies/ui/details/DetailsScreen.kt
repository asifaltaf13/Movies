package com.asifaltaf.movies.ui.details

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
    val detailState by viewModel.detailState.collectAsState()
    val movieDetail = detailState.movieDetail

    val context = LocalContext.current

    val toastMessage by viewModel.toastMessage.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            modifier = Modifier.padding(top = 16.dp),
            text = stringResource(R.string.details),
            fontFamily = Phosphate,
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleLarge
        )

        Card(
            modifier = Modifier.padding(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 16.dp)
            ) {

                Card(
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                ) {
                    AsyncImage(
                        modifier = Modifier.fillMaxSize(),
                        model = ImageRequest.Builder(context = LocalContext.current)
                            .data(movieDetail.poster)
                            .crossfade(true)
                            .build(),
                        contentDescription = stringResource(id = R.string.poster),
                        contentScale = ContentScale.Crop,
                        //error = painterResource(id = R.drawable.ic_broken_image)
                    )
                }

                Spacer(modifier = Modifier.padding(8.dp))

                Surface(
                    modifier = Modifier.padding(),
                    shape = RoundedCornerShape(16.dp),
                    shadowElevation = 8.dp,
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Column(
                        modifier = Modifier.padding(
                            start = 16.dp,
                            end = 16.dp,
                            top = 0.dp,
                            bottom = 32.dp
                        ),
                    ) {
                        TitleProperty(movieDetail.title)

                        DetailProperty("Type", movieDetail.type)
                        Spacer(modifier = Modifier.padding(bottom = 24.dp))
                        DetailProperty("Year", movieDetail.year)

                        if (isLoading) {
                            ShowLoading()
                        } else {
                            Spacer(modifier = Modifier.padding(bottom = 24.dp))
                            DetailProperty("IMDb Rating", movieDetail.imdbRating)

                            Spacer(modifier = Modifier.padding(bottom = 24.dp))
                            DetailProperty("Genre", movieDetail.genre)

                            Spacer(modifier = Modifier.padding(bottom = 24.dp))
                            DetailProperty("Country", movieDetail.country)

                            Spacer(modifier = Modifier.padding(bottom = 24.dp))
                            DetailProperty("Language", movieDetail.language)

                            Spacer(modifier = Modifier.padding(bottom = 24.dp))
                            DetailProperty("Rated", movieDetail.rated)

                            Spacer(modifier = Modifier.padding(bottom = 24.dp))
                            DetailProperty("Released", movieDetail.released)

                            Spacer(modifier = Modifier.padding(bottom = 24.dp))
                            DetailProperty("Actors", movieDetail.actors)

                            Spacer(modifier = Modifier.padding(bottom = 24.dp))
                            DetailProperty("Director", movieDetail.director)

                            Spacer(modifier = Modifier.padding(bottom = 24.dp))
                            DetailProperty("Production", movieDetail.production)

                            Spacer(modifier = Modifier.padding(bottom = 24.dp))
                            DetailProperty("Writer", movieDetail.writer)

                            Spacer(modifier = Modifier.padding(bottom = 24.dp))
                            DetailProperty("Box Office", movieDetail.boxOffice)

                            Spacer(modifier = Modifier.padding(bottom = 24.dp))
                            DetailProperty("Awards", movieDetail.awards)

                            Spacer(modifier = Modifier.padding(bottom = 24.dp))
                            DetailProperty("IMDb votes", movieDetail.imdbVotes)

                            Spacer(modifier = Modifier.padding(bottom = 24.dp))
                            DetailProperty("Metascore", movieDetail.metascore)

                            Spacer(modifier = Modifier.padding(bottom = 24.dp))
                            DetailProperty("Runtime", movieDetail.runtime)

                            Spacer(modifier = Modifier.padding(bottom = 24.dp))
                            DetailProperty("DVD", movieDetail.dVD)

                            Spacer(modifier = Modifier.padding(bottom = 24.dp))
                            DetailProperty("Website", movieDetail.website)

                            var ratings = ""
                            for (rating in movieDetail.ratings) {
                                ratings += "${rating.value} by ${rating.source}\n"
                            }

                            Spacer(modifier = Modifier.padding(bottom = 24.dp))
                            DetailProperty("Ratings", ratings.substringBeforeLast("\n"))

                            Spacer(modifier = Modifier.padding(bottom = 24.dp))
                            DetailProperty("Plot", movieDetail.plot)
                        }
                    }
                }
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
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 32.dp),
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
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}

@Composable
fun ShowLoading() {
    Box(
        modifier = Modifier.fillMaxWidth().padding(top = 32.dp),
    ) {
        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
    }
}