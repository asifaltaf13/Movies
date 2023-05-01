package com.asifaltaf.movies.ui.home.components

import android.content.Context
import android.widget.Toast
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import com.asifaltaf.movies.R
import com.asifaltaf.movies.ui.util.Tags

@Composable
fun DeleteAllAlertDialog(
    context: Context,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        modifier = Modifier.testTag(Tags.AlertDialog),
        onDismissRequest = onDismiss,
        title = { Text(text = stringResource(R.string.delete_all_loaded_movies)) },
        confirmButton = {
            Button(onClick = {
                onConfirm()
                onDismiss()
                Toast.makeText(
                    context,
                    context.getString(R.string.all_loaded_movies_deleted),
                    Toast.LENGTH_SHORT
                ).show()
            }) {
                Text(text = stringResource(R.string.yes))
            }
        })
}