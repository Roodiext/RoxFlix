import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.viona.roxflix.data.model.Movie
import com.viona.roxflix.data.remote.RetrofitInstance
import kotlinx.coroutines.launch

class DetailViewModel : ViewModel() {

    var similarMovies by mutableStateOf<List<Movie>>(emptyList())
        private set

    fun fetchSimilarMovies(movieId: Int) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getSimilarMovies(
                    movieId = movieId,
                    apiKey = "YOUR_API_KEY"
                )
                similarMovies = response.results
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
