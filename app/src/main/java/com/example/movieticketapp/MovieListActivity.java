package com.example.movieticketapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.movieticketapp.databinding.ActivityMovieListBinding;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class MovieListActivity extends AppCompatActivity {
    ActivityMovieListBinding binding;
    MovieAdapter adapter;
    List<Movie> movieList;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMovieListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();
        movieList = new ArrayList<>();
        adapter = new MovieAdapter(movieList);
        
        binding.rvMovies.setLayoutManager(new LinearLayoutManager(this));
        binding.rvMovies.setAdapter(adapter);

        // Nút để thêm phim mẫu
        binding.btnAddSample.setOnClickListener(v -> addSampleMovies());

        loadMoviesFromFirestore();
    }

    private void addSampleMovies() {
        Movie m1 = new Movie("Doraemon", "Hoạt hình", "https://example.com/doraemon.jpg");
        Movie m2 = new Movie("John Wick", "Hành động", "https://example.com/johnwick.jpg");
        Movie m3 = new Movie("Mai", "Tình cảm", "https://example.com/mai.jpg");

        db.collection("movies").add(m1);
        db.collection("movies").add(m2);
        db.collection("movies").add(m3).addOnSuccessListener(aVoid -> {
            Toast.makeText(this, "Đã thêm 3 phim mẫu. Hãy vuốt/mở lại để xem!", Toast.LENGTH_SHORT).show();
            loadMoviesFromFirestore();
        });
    }

    private void loadMoviesFromFirestore() {
        db.collection("movies")
            .get()
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    movieList.clear();
                    if (task.getResult().isEmpty()) {
                        Log.d("Firestore", "No movies found in 'movies' collection");
                        Toast.makeText(this, "Không tìm thấy phim nào trong database!", Toast.LENGTH_SHORT).show();
                    }
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Movie movie = document.toObject(Movie.class);
                        movieList.add(movie);
                        Log.d("Firestore", "Loaded movie: " + movie.getTitle());
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    Log.e("Firestore", "Error getting documents: ", task.getException());
                    Toast.makeText(this, "Lỗi tải dữ liệu: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            });
    }
}