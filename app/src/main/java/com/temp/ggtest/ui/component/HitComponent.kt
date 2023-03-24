package com.temp.ggtest.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.temp.ggtest.R
import com.temp.ggtest.data.CallResult
import com.temp.ggtest.data.model.Author
import com.temp.ggtest.data.model.Hit
import com.temp.ggtest.ui.misc.urlId
import com.temp.ggtest.ui.theme.CardBackground
import com.temp.ggtest.ui.theme.TextError
import com.temp.ggtest.util.Utils.getDateFormat
import com.temp.ggtest.viewmodel.HitViewModel
import com.temp.ggtest.viewmodel.HomeViewModel
import com.valentinilk.shimmer.shimmer
import java.time.LocalDateTime

/**
 * entry point for Hit view UI page
 * @param navController navigation controller to enable forward navigation
 */
@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun HitComponent() {
    val viewModel: HitViewModel = hiltViewModel()
    val callResult = viewModel.hit.collectAsStateWithLifecycle(initialValue = CallResult.idle())
    HitPage(callResult) {
        viewModel.refresh()
    }
}

/**
 * entry point for Hit view UI page
 * @param callResult result of a call for data
 * @param refresh handle to call data refresh
 */
@Composable
private fun HitPage(callResult: State<CallResult<Hit>>, refresh: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(20.dp,0.dp)
        .verticalScroll(rememberScrollState())) {
        Spacer(modifier = Modifier.height(5.dp))
        callResult.value.let {
            when (it.status) {
                CallResult.Status.SUCCESS -> {
                    requireNotNull(it.data)
                    Hit(it.data)
                }
                CallResult.Status.LOADING, CallResult.Status.IDLE -> {
                    LoadHit()
                }
                CallResult.Status.ERROR -> {
                    Text(
                        text = stringResource(id = R.string.err_not_found),
                        style = TextStyle(color = TextError)
                    )
                }
            }
        }
    }
}

/**
 * Component that composes hit data
 * @param hit data of the actual hit
 */
@Composable
private fun Hit(hit : Hit) {
    Column(modifier = Modifier.fillMaxSize()) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(hit.thumbUrl)
                .crossfade(true)
                .build(),
            placeholder = painterResource(R.drawable.ic_launcher_foreground),
            contentDescription = stringResource(R.string.desc_img_hit),
            modifier = Modifier
                .semantics { urlId = hit.thumbUrl }
                .clip(RoundedCornerShape(5.dp))
                .fillMaxWidth()
                .aspectRatio(1f)
                .background(color = Color.White)
        )
        Spacer(modifier = Modifier.height(5.dp))
        LabeledText(lbl = R.string.lbl_id, text = hit.id)
        hit.title?.let {
            LabeledText(lbl = R.string.lbl_title, text = it)
        }
        hit.type?.let {
            LabeledText(lbl = R.string.lbl_type, text = it)
        }
        hit.dateCreated?.let {
            LabeledText(lbl = R.string.lbl_dateCreated, text = it.format(getDateFormat()))
        }
        hit.dateModified?.let {
            LabeledText(lbl = R.string.lbl_dateModified, text = it.format(getDateFormat()))
        }
        hit.creator.authorId.let {
            LabeledText(lbl = R.string.lbl_author_id, text = it.toString())
        }
        hit.creator.name?.let {
            LabeledText(lbl = R.string.lbl_author_name, text = it)
        }

    }
}

/**
 * Component that shows a skeleton loading instead of hit
 */
@Composable
private fun LoadHit() {
    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(5.dp))
                .fillMaxWidth()
                .aspectRatio(1f)
                .background(color = Color.White)
                .shimmer()
        )
        Spacer(modifier = Modifier.height(5.dp))
        Box(modifier = Modifier.padding(0.dp,5.dp).height(20.dp)
            .background(color = CardBackground).fillMaxWidth(0.4f).shimmer())
        Box(modifier = Modifier.padding(0.dp,5.dp).height(20.dp)
            .background(color = CardBackground).fillMaxWidth(0.8f).shimmer())
        Box(modifier = Modifier.padding(0.dp,5.dp).height(20.dp)
            .background(color = CardBackground).fillMaxWidth(0.5f).shimmer())
        Box(modifier = Modifier.padding(0.dp,5.dp).height(20.dp)
            .background(color = CardBackground).fillMaxWidth(0.3f).shimmer())
        Box(modifier = Modifier.padding(0.dp,5.dp).height(20.dp)
            .background(color = CardBackground).fillMaxWidth(0.3f).shimmer())
        Box(modifier = Modifier.padding(0.dp,5.dp).height(20.dp)
            .background(color = CardBackground).fillMaxWidth(0.7f).shimmer())
    }
}

/**
 * Preview of the entire page
 */
@Preview(showBackground = true)
@Composable
private fun PagePreview() {
    val item = Hit(
        "m9cvbqgp",
        "Parallel Lines in the Coordinate Plane: Quick Exploration",
        "ws",
        LocalDateTime.parse("2020-04-27T13:58:26"),
        LocalDateTime.parse("2020-05-14T19:10:03"),
        true,
        "https://www.geogebra.org/resource/m9cvbqgp/jwu26zO0VaMqZUpt/material-m9cvbqgp-thumb1.png",
        Author(5743822, "GeoGebra Team", "/u/geogebrateam")
    )
    val hit = produceState(initialValue = CallResult.success(item)) {
        value = CallResult.success(item)
    }
    HitPage(hit) {}
}

/**
 * Preview of loading component
 */
@Preview(showBackground = true)
@Composable
private fun LoadPreview() {
    LoadHit()
}