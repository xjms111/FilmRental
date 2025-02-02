package com.xjms111;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.xjms111.entities.Movie;
import com.xjms111.entities.Rental;
import com.xjms111.entities.Review;
import com.xjms111.entities.User;
import com.xjms111.services.MovieService;
import com.xjms111.services.RentalService;
import com.xjms111.services.ReviewService;
import com.xjms111.services.RoleService;
import com.xjms111.services.UserRoleService;
import com.xjms111.services.UserService;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale.Category;


/**
 * Hello world!
 */
@SpringBootApplication
public class App extends Application {
private User myUser;
private Rental myRental;
private String myRole;
    private ConfigurableApplicationContext springContext;
    private RoleService roleService;
    private UserService userService;
    private UserRoleService userRoleService;
    private ReviewService reviewService;
    private RentalService rentalService;
    private MovieService movieService;
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage){
        loginStage(primaryStage);
    }

private void userClient(Stage primaryStage){
    VBox vBox = new VBox();
    Button showAllMoviesButton = new Button("Show all movies");
    Button refreshButton = new Button("Refresh");
    Button viewRentalsButton = new Button("View My Rentals");
    Button showAvailableMoviesButton = new Button("Show available movies");
    showAvailableMoviesButton.setOnAction(actionEvent -> {
        vBox.getChildren().clear();
        List<Movie> AvailableMovies = movieService.getAvailableMovies();
        displayMovies(vBox, AvailableMovies, primaryStage);
    });
    viewRentalsButton.setOnAction(actionEvent -> {
        List<Rental> rentals = rentalService.getRentalsByUserId(myUser.getId());
        displayRentals(primaryStage, rentals);
    });
    refreshButton.setOnAction(actionEvent -> {
        List<Movie> refreshedMovies = movieService.getAllMovies();
        //movieName.clear();
        vBox.getChildren().clear();
        displayMovies(vBox, refreshedMovies, primaryStage);
    });
    vBox.getChildren().addAll(showAllMoviesButton, refreshButton, viewRentalsButton, showAvailableMoviesButton);
    List<Movie> allMovies = movieService.getAllMovies();
    displayMovies(vBox, allMovies, primaryStage);
    Scene scene = new Scene(vBox,900,900);
    primaryStage.setTitle("Movie Rental System");
    primaryStage.setScene(scene);
    primaryStage.show();
}

private void displayMovies(VBox vBox, List<Movie> movies, Stage primaryStage){
    for (Movie movie : movies) {
        VBox movieBox = new VBox();
        Label details = new Label(movie.getTitle() + "; " + movie.getGenre() + "; Year: " + movie.getReleaseYear() + "; Available: " + movie.isAvailable());
        Button rentButton = new Button("Rent");
        Button writeReviewButton = new Button("Write Review");
        Button seeReviewsButton = new Button("See Reviews");
        rentButton.setOnAction(actionEvent -> {
            if(movie.isAvailable()) {
                rentalService.rentMovie(myUser, movie);
                refreshMovies(vBox, primaryStage);
            }
        });
        writeReviewButton.setOnAction(actionEvent -> reviewScene(movie));
        seeReviewsButton.setOnAction(actionEvent -> {
            List<Review> reviews = reviewService.getReviewsByMovieId(movie.getId());
            displayReviews(primaryStage, reviews, movie);
        });
        movieBox.getChildren().addAll(details, rentButton, writeReviewButton, seeReviewsButton);
        vBox.getChildren().add(movieBox);
    }
}
private void displayReviews(Stage primaryStage, List<Review> reviews, Movie movie){
    Stage reviewStage = new Stage();
    VBox vBox = new VBox();
    reviewStage.setTitle("Reviews for " + movie.getTitle());
    for (Review review : reviews) {
        Label reviewLabel = new Label("Rating: " + review.getRating() + "/5 - " + review.getComment());
        vBox.getChildren().add(reviewLabel);
    }
    reviewStage.setScene(new Scene(vBox, 400, 400));
    reviewStage.show();
}

private void displayRentals(Stage primaryStage, List<Rental> rentals){
    Stage rentalStage = new Stage();
    VBox vBox = new VBox();
    rentalStage.setTitle("My Rentals");
    for (Rental rental : rentals) {
        Label rentalLabel = new Label("Movie: " + rental.getMovie().getTitle() + ", Rented on: " + rental.getRentalDate());
        vBox.getChildren().add(rentalLabel);
    }
    rentalStage.setScene(new Scene(vBox, 400, 400));
    rentalStage.show();
}

private void reviewScene(Movie movie){
    Stage reviewStage = new Stage();
    VBox vBox = new VBox();
    reviewStage.setTitle("Review");
    Label scoreLabel = new Label("Rating: ");
    ComboBox<Integer> score = new ComboBox<>();
    score.getItems().addAll(1, 2, 3, 4, 5);
    score.setValue(5);
    TextArea commentArea = new TextArea();
    commentArea.setPromptText("Write your opinion here");
    Button confirmButton = new Button("Confirm");
    confirmButton.setOnAction(actionEvent -> {
        Review review = new Review();
        reviewService.saveReview(review);
        reviewStage.close();
    });
    vBox.getChildren().addAll(scoreLabel, score, commentArea, confirmButton);
    reviewStage.setScene(new Scene(vBox, 400, 400));
    reviewStage.show();
}

private void refreshMovies(VBox vBox, Stage primaryStage){
    List<Movie> refreshedMovies = movieService.getAllMovies();
    vBox.getChildren().clear();
    displayMovies(vBox, refreshedMovies, primaryStage);
}

private void loginStage(Stage primaryStage) {
    Stage loginStage = new Stage();
    loginStage.setTitle("Login");

    VBox vBox = new VBox();
    TextField useremailField = new TextField();
    useremailField.setPromptText("Enter your username");
    PasswordField passwordField = new PasswordField();
    passwordField.setPromptText("Enter your password");
    Button loginButton = new Button("Login");

    Label errorLabel = new Label();
    
    loginButton.setOnAction(actionEvent -> {
        String useremail = useremailField.getText();
        String password = passwordField.getText();

        User user = userService.getUserByEmail(useremail);
        if (user != null && user.getPassword().equals(password)) {
            myUser = user;
            loginStage.close();
            userClient(primaryStage);
        } else {
            errorLabel.setText("Invalid username or password");
        }
    });

    vBox.getChildren().addAll(useremailField, passwordField, loginButton, errorLabel);
    Scene scene = new Scene(vBox, 300, 200);
    loginStage.setScene(scene);
    loginStage.initOwner(primaryStage);
    loginStage.initModality(Modality.APPLICATION_MODAL);
    loginStage.showAndWait();
}

@Override
public void init() throws Exception{
    springContext=SpringApplication.run(App.class);
    roleService = springContext.getBean(RoleService.class);
    userService = springContext.getBean(UserService.class);
    userRoleService = springContext.getBean(UserRoleService.class);
    reviewService = springContext.getBean(ReviewService.class);
    rentalService = springContext.getBean(RentalService.class);
    movieService = springContext.getBean(MovieService.class);
}

@Override
public void stop() throws Exception {
    springContext.close();
}
}
