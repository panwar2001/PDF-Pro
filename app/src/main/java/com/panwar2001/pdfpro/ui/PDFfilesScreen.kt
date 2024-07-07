package com.panwar2001.pdfpro.ui
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.panwar2001.pdfpro.R

data class PdfRow(
    val dateModified: String,
    val name: String,
    val size: Float,
    val id: Long
)
@Composable
fun PdfFilesScreen(listPDF: List<PdfRow>,
                   shareFile:(Long)->Unit,
                   onPdfCardClick:(Long)->Unit,
                   ) {
     LazyColumn {
            items(listPDF) { pdfItem ->
                ElevatedCard(
                    elevation = CardDefaults.cardElevation(defaultElevation = 15.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                        .clickable { onPdfCardClick(pdfItem.id) },
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Row(Modifier.padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically){
                        Image(painter = painterResource(id = R.drawable.pdf_svg),
                            contentDescription = null,
                            modifier = Modifier.size(50.dp))
                        Column {
                            Row(Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween){
                                Text(
                                    text = pdfItem.name,
                                    overflow = TextOverflow.Ellipsis,
                                    maxLines = 1,
                                    modifier=Modifier.weight(1f))
                                Icon(
                                    imageVector = Icons.Default.Share,
                                    modifier = Modifier.clickable {shareFile(pdfItem.id)},
                                    contentDescription = null
                                )
                            }
                            Row{
                                Text("${pdfItem.size} MB")
                                Spacer(modifier = Modifier.width(20.dp))
                                Text(pdfItem.dateModified)
                            }
                        }

                    }
                }
            }
     }
}

@Composable
@Preview
fun PreviewPdf(){
    val list= mutableListOf<PdfRow>()
    repeat(20){
     list.add(PdfRow("18/03/02","file_${it}.pdf",2f,0))
    }
    PdfFilesScreen(
        listPDF = list,
        shareFile = {}, {
    })
}