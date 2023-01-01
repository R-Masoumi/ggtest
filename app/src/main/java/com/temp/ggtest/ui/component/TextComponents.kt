package com.temp.ggtest.ui.component

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.temp.ggtest.R
import com.temp.ggtest.ui.theme.CardBackground

/**
 * A component of a label text with a dynamic text below
 */
@Composable
fun LabeledText(@StringRes lbl: Int, text: String) {
    Card(
        modifier = Modifier
            .padding(2.dp)
            .fillMaxWidth(),
        backgroundColor = CardBackground
    ) {
        Column(
            modifier = Modifier
                .padding(5.dp)
        ) {
            Text(
                text = stringResource(lbl),
                modifier = Modifier.padding(2.dp),
                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 15.sp)
            )
            Text(
                text = text,
                modifier = Modifier.padding(2.dp),
                style = TextStyle(fontSize = 12.sp)
            )
        }

    }
}

/**
 * A component of a title text
 */
@Composable
fun TitleText(@StringRes title: Int, modifier: Modifier = Modifier) {
    Text(
        text = stringResource(title),
        modifier = modifier,
        style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp, textAlign = TextAlign.Center)
    )
}

/**
 * A component of a text with optional composable elements around it,
 * mimics DrawableTop/Start/End/Bottom in legacy xml views
 */
@Composable
fun DrawableWrapper(
    modifier: Modifier = Modifier,
    drawableTop: (@Composable () -> Unit)? = null,
    drawableStart: (@Composable () -> Unit)? = null,
    drawableBottom: (@Composable () -> Unit)? = null,
    drawableEnd: (@Composable () -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    ConstraintLayout(modifier) {
        val (refImgStart, refImgTop, refImgBottom, refImgEnd, refContent) = createRefs()
        Box(Modifier.constrainAs(refContent) {
            top.linkTo(drawableTop?.let { refImgTop.bottom } ?: parent.top)
            bottom.linkTo(drawableBottom?.let { refImgBottom.top } ?: parent.bottom)
            start.linkTo(drawableStart?.let { refImgStart.end } ?: parent.start)
            end.linkTo(drawableEnd?.let { refImgEnd.start } ?: parent.end)
        }) {
            content()
        }
        drawableTop?.let {
            Box(
                Modifier.constrainAs(refImgTop) {
                    top.linkTo(parent.top)
                    start.linkTo(refContent.start)
                    end.linkTo(refContent.end)
                }
            ) {
                it()
            }
        }
        drawableStart?.let {
            Box(
                Modifier.constrainAs(refImgStart) {
                    top.linkTo(refContent.top)
                    bottom.linkTo(refContent.bottom)
                    start.linkTo(parent.start)
                }
            ) {
                it()
            }
        }
        drawableBottom?.let {
            Box(
                Modifier.constrainAs(refImgBottom) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(refContent.start)
                    end.linkTo(refContent.end)
                }
            ) {
                it()
            }
        }
        drawableEnd?.let {
            Box(
                Modifier.constrainAs(refImgEnd) {
                    top.linkTo(refContent.top)
                    bottom.linkTo(refContent.bottom)
                    end.linkTo(parent.end)
                }
            ) {
                it()
            }
        }
    }
}