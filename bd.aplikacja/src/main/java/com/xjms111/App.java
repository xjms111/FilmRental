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


//admin
    public void adminClient(Stage primaryStage){
        VBox vBox = new VBox();
        Label booksLabel = new Label("Moderate books:");
         Button showMoviesButton = new Button("Books");
        Label usersLabel = new Label("Moderate users:");
        Button createUserButton = new Button("Create User");
        Label categoriesLabel = new Label("Moderate categories:");
        Button showCategoriesButton = new Button("Categories");
        Label reviewsLabel = new Label("Moderate reviews:");
        Button showReviewsButton = new Button("Reviews");
        showMoviesButton.setOnAction(actionEvent -> {
            List<Movie> movies = movieService.getAllMovies();
            displayMoviesForAdmin(primaryStage,movies);
        });
        //    createUserButton.setOnAction(actionEvent -> {
         //   Stage createUserStage = new Stage();
         //   createUserStage.setTitle("Create User");
          //  createUserStage.initModality(Modality.APPLICATION_MODAL);
          //  registerStage(createUserStage);
        //});
        // showCategoriesButton.setOnAction(actionEvent -> {
          //   List<Category> categories = categoryService.getAllCategories();
           //  displayCategories(primaryStage, categories);
        //});
        showReviewsButton.setOnAction(actionEvent -> {
            List<Review> reviews = reviewService.getAllReviews();
            displayReviews(primaryStage, reviews, null);
        });
        vBox.getChildren().addAll(booksLabel, showMoviesButton, usersLabel, createUserButton, categoriesLabel,
                showCategoriesButton, reviewsLabel, showReviewsButton);
        primaryStage.setTitle("Admin Client");
        primaryStage.setScene(new Scene(vBox, 800, 800));
        primaryStage.show();
    }

    private void displayMoviesForAdmin(Stage primaryStage, List<Movie> movies){
        VBox booksBox = new VBox();
        HBox buttonsBox = new HBox();
        TextField bookName = new TextField();
        bookName.setPromptText("Enter the book name or author");
        Button searchButton = new Button("Search");
        Button backButton = new Button("Back");
        Button addButton = new Button("Add book");
        buttonsBox.getChildren().addAll(bookName, searchButton, backButton, addButton);
        booksBox.getChildren().add(buttonsBox);
        searchButton.setOnAction(actionEvent -> {
            List<Book> filteredBooks = booksService.getBooksByTitleOrAuthorOrCategory(bookName.getText());
            displayBooksForAdmin(primaryStage, filteredBooks);
        });
        backButton.setOnAction(actionEvent -> {
            if(myRole == Role.ADMIN) {
                adminClient(primaryStage);
            } else if(myRole == Role.EMPLOYEE){
                employeeClient(primaryStage);
            }
        });
        addButton.setOnAction(actionEvent -> {
            addBookScene(primaryStage);
        });
        for (Book book : books) {
            HBox bookHBox = new HBox();
            Label details = new Label(book.getTitle() + "; " + book.getAuthor() + "; category: " + book.getCategory().getName() +
                    "; Price: " + book.getPrice() + " $; Stock: " +  book.getStock());
            Button editButton = new Button("Edit");
            Button deleteButton = new Button("Delete");
            editButton.setOnAction(actionEvent -> editBookScene(book, primaryStage));
            deleteButton.setOnAction(actionEvent -> {
                booksService.deleteBook(book);
                displayBooksForAdmin(primaryStage, booksService.getAllBooks());
            });
            bookHBox.getChildren().addAll(details, editButton, deleteButton);
            booksBox.getChildren().add(bookHBox);
        }
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(booksBox);
        primaryStage.setScene(new Scene(scrollPane, 700, 700));
        primaryStage.show();
    }

    private void displayCategories(Stage primaryStage, List<Category> categories){
        VBox categoriesBox = new VBox();
        Button backButton = new Button("Back");
        Button addButton = new Button("Add category");
        categoriesBox.getChildren().addAll(backButton, addButton);
        backButton.setOnAction(actionEvent -> {
            adminClient(primaryStage);
        });
        addButton.setOnAction(actionEvent -> {
            addCategoryScene(primaryStage);
        });
        for (Category category : categories) {
            HBox categoryHBox = new HBox();
            Label categoryName = new Label(category.getName());
            Button deleteButton = new Button("Delete");
            deleteButton.setOnAction(actionEvent -> {
                categoryService.deleteCategory(category);
                displayCategories(primaryStage, categoryService.getAllCategories());
            });
            categoryHBox.getChildren().addAll(categoryName, deleteButton);
            categoriesBox.getChildren().add(categoryHBox);
        }
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(categoriesBox);
        primaryStage.setScene(new Scene(scrollPane, 700, 700));
        primaryStage.show();
    }

    private void addCategoryScene(Stage primaryStage){
        Stage stage = new Stage();
        VBox vBox = new VBox();
        stage.setTitle("Add category");
        stage.initModality(Modality.APPLICATION_MODAL);
        Label categoryName = new Label("Category: ");
        TextField categoryField = new TextField();
        Button addButton = new Button("Add category");
        addButton.setOnAction(actionEvent -> {
            Category category = new Category();
            category.setName(categoryField.getText());
            if(!categoryField.getText().isEmpty()) {
                categoryService.addCategory(category);
                displayCategories(primaryStage, categoryService.getAllCategories());
                stage.close();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Please enter a valid category name");
            }
        });
        vBox.getChildren().addAll(categoryName, categoryField, addButton);
        stage.setScene(new Scene(vBox, 800, 800));
        stage.show();
    }

    private void addBookScene(Stage primaryStage){
        Stage stage = new Stage();
        VBox vBox = new VBox();
        stage.setTitle("Add Book");
        stage.initModality(Modality.APPLICATION_MODAL);
        Label title = new Label("Title: ");
        Label author = new Label("Author: ");
        Label ISBN = new Label("ISBN: ");
        Label price = new Label("Price: ");
        Label category = new Label("Category: ");
        Label stock = new Label("Stock: ");
        TextField titleField = new TextField();
        TextField authorField = new TextField();
        TextField ISBNField = new TextField();
        TextField priceField = new TextField();
        ComboBox<String> categoryField = new ComboBox<>();
        TextField stockField = new TextField();
        List<Category> categories = categoryService.getAllCategories();
        Button addButton = new Button("Add");
        addButton.setOnAction(actionEvent -> {
            Book book = new Book();
            addBook(titleField, authorField, ISBNField, priceField, categoryField, stockField, book, stage, primaryStage);
        });
        for(Category category1 : categories){
            categoryField.getItems().add(category1.getName());
        }
        vBox.getChildren().addAll(title,titleField, author, authorField, ISBN, ISBNField, price, priceField,
                category, categoryField, stock, stockField, addButton);
        stage.setScene(new Scene(vBox, 800, 800));
        stage.show();
    }

    private void editBookScene(Book book, Stage primaryStage){
        Stage editStage = new Stage();
        VBox editBox = new VBox();
        editStage.setTitle("Edit Book");
        editStage.initModality(Modality.APPLICATION_MODAL);
        Label title = new Label("Title: ");
        Label author = new Label("Author: ");
        Label ISBN = new Label("ISBN: ");
        Label price = new Label("Price: ");
        Label category = new Label("Category: ");
        Label stock = new Label("Stock: ");
        TextField titleField = new TextField(book.getTitle());
        TextField authorField = new TextField(book.getAuthor());
        TextField ISBNField = new TextField(book.getIsbn());
        TextField priceField = new TextField(book.getPrice().toString());
        ComboBox<String> categoryField = new ComboBox<>();
        TextField stockField = new TextField(String.valueOf(book.getStock()));
        Button editButton = new Button("Edit");
        List<Category> categories = categoryService.getAllCategories();
        for(Category category1 : categories){
            categoryField.getItems().add(category1.getName());
        }
        editButton.setOnAction(actionEvent -> {
            addBook(titleField, authorField, ISBNField, priceField, categoryField, stockField, book, editStage, primaryStage);
        });
        editBox.getChildren().addAll(title,titleField, author, authorField, ISBN, ISBNField, price, priceField,
                category, categoryField, stock, stockField, editButton);
        editStage.setScene(new Scene(editBox, 700, 700));
        editStage.show();
    }

    private void addBook(TextField titleField, TextField authorField,TextField ISBNField, TextField priceField, ComboBox<String> categoryField, TextField stockField, Book book, Stage stage, Stage primaryStage){
        try {
            if (!titleField.getText().isEmpty() && !authorField.getText().isEmpty() && !ISBNField.getText().isEmpty() &&
                    !priceField.getText().isEmpty() && !stockField.getText().isEmpty() && !categoryField.getItems().isEmpty()) {
                book.setTitle(titleField.getText());
                book.setAuthor(authorField.getText());
                book.setIsbn(ISBNField.getText());
                book.setPrice(new BigDecimal(priceField.getText()));
                book.setCategory(categoryService.getCategoryByName(categoryField.getValue()));
                book.setStock(Integer.parseInt(stockField.getText()));
                String updateResult = booksService.addBook(book);
                if (updateResult.equals("Book updated")){
                    displayBooksForAdmin(primaryStage, booksService.getAllBooks());
                    stage.close();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText(updateResult);
                    alert.showAndWait();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Fields cannot be empty");
                alert.showAndWait();
            }
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please enter valid numbers in field Price and Stock.");
            alert.showAndWait();
        }
    }
    private void displayReviews(Stage primaryStage, List<Review> reviews, Book book){
        VBox allReviewsBox = new VBox();
        Button backButton = new Button("Back");
        backButton.setOnAction(actionEvent -> {
            if (myRole=="ADMIN"){
                adminClient(primaryStage);
            } else if (myRole=="USER"){
                userClient(primaryStage);
        });
        allReviewsBox.getChildren().add(backButton);
        for(Review review : reviews){
            if(myRole == Role.USER){
                if (review.getBook().getId() == book.getId()){
                    displaySingleReview(review, primaryStage, allReviewsBox, book);
                }
            } else {
                displaySingleReview(review, primaryStage, allReviewsBox, book);
            }
        }
        primaryStage.setScene(new Scene(allReviewsBox, 800, 800));
    }
    private void displaySingleReview(Review review, Stage primaryStage, VBox allReviewsBox, Book book){
        HBox hBox = new HBox();
        VBox vBox = new VBox();
        Label userLabel = new Label(review.getUser().getUsername());
        TextArea commentArea = new TextArea(review.getComment());
        commentArea.setEditable(false);
        commentArea.setPrefSize(200,100);
        Label starsLabel = new Label("  stars: " + review.getRating() + "/5");
        vBox.getChildren().addAll(starsLabel, commentArea);
        hBox.getChildren().addAll(userLabel,vBox);
        if (myRole==Role.USER){
            if (review.getUser().getId().equals(myUser.getId())){
                Button deleteMyReviewButton = new Button("Delete");
                deleteMyReviewButton.setOnAction(actionEvent -> {
                    reviewService.delete(review);
                    displayReviews(primaryStage, reviewService.findAll(), book);
                });
                hBox.getChildren().add(deleteMyReviewButton);
            }
        } else if (myRole==Role.ADMIN||myRole==Role.EMPLOYEE){
            Button deleteReviewButton = new Button("Delete");
            deleteReviewButton.setOnAction(actionEvent -> {
                reviewService.delete(review);
                displayReviews(primaryStage, reviewService.findAll(), book);
            });
            Label bookLabel = new Label("  review for: " + review.getBook().getTitle());
            hBox.getChildren().addAll(deleteReviewButton, bookLabel);
        }
        allReviewsBox.getChildren().add(hBox);
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
