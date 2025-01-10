package com.al4apps.mycomposeapp.layout

import android.widget.ImageView
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.viewinterop.AndroidView
import com.al4apps.mycomposeapp.R
import com.bumptech.glide.Glide

@Composable
fun GlideImageWithPreview(
    data: Any?,
    modifier: Modifier? = null,
    contentDescription: String? = null,
    contentScale: ContentScale = ContentScale.Fit
) {
    if (data == null) {
        Image(
            painter = painterResource(R.drawable.ic_launcher_foreground),
            contentDescription = contentDescription,
            modifier = modifier ?: Modifier,
            alignment = Alignment.Center,
            contentScale = contentScale
        )
    } else {
        AndroidView(
            factory = { context ->
                ImageView(context).apply {
                    this.contentDescription = contentDescription
                    scaleType = ImageView.ScaleType.CENTER_CROP
                }
            },
            modifier = modifier ?: Modifier,
            update = { imageView ->
                Glide.with(imageView.context)
                    .load(data)
                    .into(imageView)
            }
        )
    }

}