package com.temp.ggtest.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import com.temp.ggtest.ui.misc.Navigation.Hit.navigateToHit
import com.temp.ggtest.ui.misc.urlId
import com.temp.ggtest.ui.theme.CardBackground
import com.temp.ggtest.ui.theme.TextError
import com.temp.ggtest.viewmodel.HomeViewModel
import com.valentinilk.shimmer.shimmer
import java.time.LocalDateTime

/**
 * entry point for Home view UI page
 * @param navController navigation controller to enable forward navigation
 */
@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun HomeComponent() {
    val viewModel: HomeViewModel = hiltViewModel()
    val callResult = viewModel.hitList.collectAsStateWithLifecycle(CallResult.idle())
    HomePage(viewModel::navigate ,callResult) {
        viewModel.refresh()
    }
}

/**
 * entry point for Hit view UI page
 * @param navController navigation controller to enable forward navigation
 * @param callResult result of a call for data
 * @param refresh handle to call data refresh
 */
@Composable
private fun HomePage(navigate : (String) -> Unit, callResult: State<CallResult<List<Hit>>>, refresh: () -> Unit) {
    Column {
        Spacer(modifier = Modifier.height(5.dp))
        DrawableWrapper(drawableEnd = {
            IconButton(onClick = { refresh() }) {
                Icon(Icons.Filled.Refresh, contentDescription = stringResource(id = R.string.desc_img_refresh))
            }
        }, modifier = Modifier.fillMaxWidth()) {
            TitleText(title = R.string.lbl_hit_list, modifier = Modifier.fillMaxWidth())
        }
        Spacer(modifier = Modifier.height(5.dp))
        callResult.value.let {
            when (it.status) {
                CallResult.Status.SUCCESS -> {
                    val list = it.data
                    if (list.isNullOrEmpty()) {
                        Text(
                            text = stringResource(R.string.info_no_hit_found),
                            modifier = Modifier.fillMaxWidth()
                        )
                    } else {
                        HitList(list = list, navigate)
                    }
                }
                CallResult.Status.LOADING, CallResult.Status.IDLE -> {
                    LoadingList()
                }
                CallResult.Status.ERROR -> {
                    Text(
                        text = stringResource(R.string.info_no_hit_found),
                        modifier = Modifier.fillMaxWidth(),
                        style = TextStyle(color = TextError)
                    )
                }
            }
        }
    }
}

/**
 * Component that composes hit data
 * @param list data of list of hits
 * @param navController navigation controller to enable forward navigation
 */
@Composable
private fun HitList(list: List<Hit>, navigate : (String) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(list) { hit ->
            HitItem(hit, navigate)
        }
    }
}

/**
 * Component that composes one hit item
 * @param hit data of the actual hit
 * @param navController navigation controller to enable forward navigation
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun HitItem(hit: Hit, navigate : (String) -> Unit) {
    Card(
        onClick = { navigate(hit.id) },
        modifier = Modifier.padding(5.dp)
    ) {
        Column {
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
            hit.title?.let {
                Text(
                    text = hit.title,
                    modifier = Modifier
                        .padding(2.dp, 5.dp)
                        .fillMaxWidth(),
                    style = TextStyle(fontSize = 12.sp, textAlign = TextAlign.Center)
                )
            }
        }
    }

}

/**
 * Component that shows a skeleton loading of a list
 */
@Composable
private fun LoadingList() {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(5) { _ ->
            LoadingItem()
        }
    }
}

/**
 * Component that composes loading view of one item
 */
@Composable
private fun LoadingItem() {
    Card(
        modifier = Modifier.padding(5.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(5.dp))
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .background(CardBackground)
                    .shimmer()
            )
            Box(
                modifier = Modifier
                    .padding(2.dp, 5.dp)
                    .height(12.dp)
                    .fillMaxWidth(0.6f)
                    .background(CardBackground)
                    .shimmer()
            )
        }
    }
}

/**
 * Preview of the entire page
 */
@Preview(showBackground = true)
@Composable
private fun PagePreview() {
    val hit = Hit(
        "m9cvbqgp",
        "Parallel Lines in the Coordinate Plane: Quick Exploration",
        "ws",
        LocalDateTime.parse("2020-04-27T13:58:26"),
        LocalDateTime.parse("2020-05-14T19:10:03"),
        true,
        "https://www.geogebra.org/resource/m9cvbqgp/jwu26zO0VaMqZUpt/material-m9cvbqgp-thumb1.png",
        Author(5743822, "GeoGebra Team", "/u/geogebrateam")
    )
    val list = produceState(initialValue = CallResult.success(listOf(hit, hit, hit))) {
        value = CallResult.success(listOf(hit))
    }
    HomePage({}, list) {}
}

/**
 * Preview of loading component
 */
@Preview(showBackground = true)
@Composable
private fun LoadPreview() {
    LoadingList()
}