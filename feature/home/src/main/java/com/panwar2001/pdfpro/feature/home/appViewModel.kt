//package com.panwar2001.pdfpro.feature.home
//
//import androidx.annotation.WorkerThread
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.panwar2001.pdfpro.data.ToolsInterface
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.flow.update
//import kotlinx.coroutines.launch
//import javax.inject.Inject
//
///**
// * Data class that represents the current UI state
// */
//
//
//@HiltViewModel
//class AppViewModel @Inject constructor(private val toolsRepository: ToolsInterface): ViewModel() {
//    private val _uiState = MutableStateFlow(toolsRepository.initAppUiState())
//    val uiState: StateFlow<AppUiState> = _uiState.asStateFlow()
//    val options= listOf(
//        R.string.sort_by_date,
//        R.string.sort_by_size,
//        R.string.sort_by_name)
//    val languages = listOf(
//            R.array.english,
//            R.array.French,
//            R.array.Japanese,
//            R.array.Russian,
//            R.array.hindi)
//
////    init {
////        searchPdfs()
////    }
//    @WorkerThread
//     fun getCurrentLocale(): String {
//         return toolsRepository.getAppLocale()
//    }
//    fun setSearchText(query: String){
//        _uiState.update {
//            it.copy(query=query)
//        }
//    }
//    fun toggleSortOrder(){
//        _uiState.update {
//            it.copy(isAscending =!it.isAscending)
//        }
//    }
//    private fun setPdfsList(pdfsList:List<PdfRow>){
//        _uiState.update {
//            it.copy(pdfsList = pdfsList)
//        }
//    }
//    fun setSearchBarActive(active:Boolean){
//        _uiState.update {
//            it.copy(searchBarActive = active)
//        }
//    }
// fun setUri(uriId:Long){
//     try {
//         viewModelScope.launch {
//             _uiState.update {
//                 val uri=toolsRepository.getUriFromMediaId(uriId)
//                 it.copy(
//                     pdfUri = uri,
//                     numPages = toolsRepository.getNumPages(uri),
//                     pdfName = toolsRepository.getPdfName(uri)
//                 )
//             }
//         }
//     }catch(e: Exception){
//         e.printStackTrace()
//     }
//    }
//    fun setBottomSheetVisible(visible:Boolean){
//        _uiState.update {
//            it.copy(isBottomSheetVisible = visible)
//        }
//    }
//    fun setSortOption(sortOption:Int){
//        _uiState.update {
//            it.copy(sortOption = sortOption)
//        }
//    }
//
//     fun setLocale(localeTag: String) {
//        viewModelScope.launch {
//            toolsRepository.setAppLocale(localeTag)
//        }
//     }
//    fun searchPdfs(){
////            viewModelScope.launch {
////                    setPdfsList(
////                        toolsRepository.searchPdfs(
////                            sortingOrder = uiState.value.sortOption,
////                            ascendingSort = uiState.value.isAscending,
////                            mimeType = "application/pdf",
////                            query = uiState.value.query
////                        )
////                    )
////            }
//    }
//
//}