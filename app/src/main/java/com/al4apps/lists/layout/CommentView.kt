package com.al4apps.lists.layout

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class Comment(
    val image: String?,
    val name: String,
    val text: String
)

@Composable
fun CommentView(comment: Comment) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(6.dp)
    ) {
        GlideImageWithPreview(comment.image, Modifier.size(60.dp))
        Column(modifier = Modifier.padding(start = 6.dp)) {
            Text(
                text = comment.name,
                maxLines = 1,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )
            Text(
                text = comment.text,
                maxLines = 2,
                fontSize = 12.sp,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CommentPreview() {
    CommentView(Comment(null, "Alisher", "Hello World!"))
}

@Composable
fun GenreItem(genre: String) {
    Column(
        modifier = Modifier.apply {
            fillMaxWidth()
        }
    ) {
        Text(
            text = genre,
            fontSize = 16.sp
        )
    }
}

@Preview()
@Composable
fun PreviewItems() {
    MaterialTheme {
        GenreItem("Приключения")
    }
}