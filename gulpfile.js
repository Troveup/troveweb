var gulp = require('gulp'),
    sass = require('gulp-sass'),
    del = require('del'),
    minifycss = require('gulp-minify-css');

var sourceFiles = [
    './src/main/webapp/WEB-INF/resources/sass/main.scss'
];

var filesToClean = './target/brooklyn-1.0-SNAPSHOT/WEB-INF/resources/stylesheets/main.css'
var compiledCSSDest = './target/brooklyn-1.0-SNAPSHOT/WEB-INF/resources/stylesheets/'

gulp.task('sass', function() {
  del([filesToClean], function(err) {
    if (err) return;
    return gulp.src(sourceFiles)
    .pipe(sass().on('error', sass.logError))
    .pipe(gulp.dest(compiledCSSDest));
  });
});

gulp.task('sass:watch', function () {
    gulp.watch(sourceFiles, ['sass']);
});