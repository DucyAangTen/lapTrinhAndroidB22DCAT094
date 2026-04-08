package com.example.movieticketapp;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.movieticketapp.databinding.ActivityBookingBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class BookingActivity extends AppCompatActivity {
    ActivityBookingBinding binding;
    FirebaseFirestore db;
    String movieTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBookingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();
        movieTitle = getIntent().getStringExtra("movieTitle");
        binding.tvBookingMovieTitle.setText(movieTitle);

        // Dữ liệu mẫu cho Rạp và Suất chiếu (Đề bài yêu cầu bảng này trên Firebase, sau này bạn có thể load từ đó)
        String[] theaters = {"CGV Vincom", "Lotte Cinema", "BHD Star"};
        String[] showtimes = {"10:00", "14:00", "18:00", "21:00"};

        binding.spTheaters.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, theaters));
        binding.spShowtimes.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, showtimes));

        binding.btnConfirmBooking.setOnClickListener(v -> saveTicketToFirebase());
    }

    private void saveTicketToFirebase() {
        String userId = FirebaseAuth.getInstance().getUid();
        String selectedTheater = binding.spTheaters.getSelectedItem().toString();
        String selectedShowtime = binding.spShowtimes.getSelectedItem().toString();

        Map<String, Object> ticket = new HashMap<>();
        ticket.put("userId", userId);
        ticket.put("movieTitle", movieTitle);
        ticket.put("theater", selectedTheater);
        ticket.put("showtime", selectedShowtime);
        ticket.put("status", "Confirmed");

        db.collection("tickets")
            .add(ticket)
            .addOnSuccessListener(documentReference -> {
                Toast.makeText(this, "Đặt vé thành công!", Toast.LENGTH_LONG).show();
                finish();
            })
            .addOnFailureListener(e -> Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}