package kr.ac.kumoh.s20250000.s25w08retrofit.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import kr.ac.kumoh.s20250000.s25w08retrofit.model.Song
import kr.ac.kumoh.s20250000.s25w08retrofit.viewmodel.SongViewModel


@Composable
fun SongList(
    list: List<Song>,
    onNavigateToDetail: (String) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(list, key = { it.id }) { song ->
            SongCard(song, onNavigateToDetail)
        }
    }
}

@Composable
fun SongCard(
    song: Song,
    onNavigateToDetail: (String) -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(8.dp),
        onClick = {
            onNavigateToDetail(song.id)
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Coil을 사용한 비동기 이미지 출력
            AsyncImage(
                model = "https://picsum.photos/300/300?random=${song.id}",
                contentDescription = "${song.title} 이미지",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(100.dp)
                    //.clip(CircleShape),
                    .clip(RoundedCornerShape(percent = 10)),
            )

            Spacer(Modifier.width(16.dp))

            Column(
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.SpaceAround
            ) {
                Text(
                    text = song.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                )
                Spacer(Modifier.height(16.dp))
                Text(
                    text = song.singer,
                )
            }
            Spacer(Modifier.width(16.dp))
        }
    }
}

@Composable
fun SongDetailScreen(
    songId: String?,
    viewModel: SongViewModel
) {
    if (songId.isNullOrEmpty()) {
        return
    }

    val song = viewModel.findSong(songId) ?: return

    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        AsyncImage(
            model = "https://picsum.photos/300/300?random=${song.id}",
            contentDescription = "${song.title} 이미지",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        )

        Spacer(Modifier.width(16.dp))

        RatingBar(song.rating)
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = song.title,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            fontSize = 50.sp,
            lineHeight = 60.sp,
            textAlign = TextAlign.Center,
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AsyncImage(
                model = "https://i.pravatar.cc/100?u=${song.singer}",
                contentDescription = "가수 이미지",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(30.dp)
                    .clip(CircleShape)
            )
            Text(song.singer, fontSize = 20.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        song.lyrics?.let {
            Text(
                text = it,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            )
        }
    }
}

@Composable
fun RatingBar(stars: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
    ) {
        repeat(stars) {
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = "별 아이콘",
                modifier = Modifier.size(36.dp),
                tint = Color.Red)
        }
    }
}