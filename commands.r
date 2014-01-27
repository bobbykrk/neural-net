library(MASS)
pts2 <- mvrnorm(n=100, c(10, 10), matrix(c(3, 0, 6, 3), 2, 2), empirical = TRUE)
pts1 <- mvrnorm(n=100, c(0, 0), matrix(c(3, 0, 6, 3), 2, 2), empirical = TRUE)
plot(c(pts1,pts2))
pts1.frame <- data.frame(pts1)
pts1.frame$col <- rep(1,nrow(pts1.frame))
pts2.frame <- data.frame(pts2)
pts2.frame$col <- rep(2,nrow(pts2.frame))
fr <- rbind(pts1.frame, pts2.frame)
write.table(fr,'training_set_1.csv', row.names=FALSE, col.names=FALSE, sep=",")
plot(fr$X1,fr$X2, col=c("red","blue")[fr$col])

pts1 <- mvrnorm(n=100, c(0, 0), matrix(c(3, 0, 6, 3), 2, 2), empirical = TRUE)
pts2 <- mvrnorm(n=100, c(10, 10), matrix(c(3, 0, 6, 3), 2, 2), empirical = TRUE)
vals <- list(pts1, pts2)

points <- function(vals, file.name){
	fr <- data.frame();
	for(i in 1:length(vals)){
		pts1.frame <- data.frame(vals[i]);
		pts1.frame$col <- rep(i,nrow(pts1.frame));
		fr <- rbind(fr, pts1.frame);
	}
	write.table(fr,file.name, row.names=FALSE, col.names=FALSE, sep=",");
	png(filename=paste(file.name, '.png', sep=""))
	plot(fr$X1,fr$X2, col=fr$col)
	dev.off()
	return(fr);
}
plot(fr$X1,fr$X2, col=c("red","blue","green")[fr$col])

pts1 <- mvrnorm(n=100, c(0, 0), matrix(c(3, 0, 6, 3), 2, 2), empirical = TRUE)
pts2 <- mvrnorm(n=100, c(10, 10), matrix(c(3, 0, 6, 3), 2, 2), empirical = TRUE)
vals <- list(pts1, pts2)
points(vals, 'set_1_2_eq');
pts1 <- mvrnorm(n=100, c(2, 2), matrix(c(3, 0, 6, 3), 2, 2), empirical = TRUE)
pts2 <- mvrnorm(n=100, c(8, 8), matrix(c(3, 0, 6, 3), 2, 2), empirical = TRUE)
vals <- list(pts1, pts2)
points(vals, 'set_2_2_eq');
pts1 <- mvrnorm(n=100, c(3, 3), matrix(c(3, 0, 6, 3), 2, 2), empirical = TRUE)
pts2 <- mvrnorm(n=100, c(7, 7), matrix(c(3, 0, 6, 3), 2, 2), empirical = TRUE)
vals <- list(pts1, pts2)
points(vals, 'set_3_2_eq');
pts1 <- mvrnorm(n=100, c(4, 4), matrix(c(3, 0, 6, 3), 2, 2), empirical = TRUE)
pts2 <- mvrnorm(n=100, c(6, 6), matrix(c(3, 0, 6, 3), 2, 2), empirical = TRUE)
vals <- list(pts1, pts2)
points(vals, 'set_4_2_eq');

pts1 <- mvrnorm(n=100, c(0, 0), matrix(c(3, 0, 6, 3), 2, 2), empirical = TRUE)
pts2 <- mvrnorm(n=100, c(10, 10), matrix(c(3, 0, 6, 3), 2, 2), empirical = TRUE)
pts3 <- mvrnorm(n=100, c(0, 10), matrix(c(3, 0, 6, 3), 2, 2), empirical = TRUE)
vals <- list(pts1, pts2, pts3)
points(vals, 'set_5_3_2_eq');
pts1 <- mvrnorm(n=100, c(2, 2), matrix(c(3, 0, 6, 3), 2, 2), empirical = TRUE)
pts2 <- mvrnorm(n=100, c(8, 8), matrix(c(3, 0, 6, 3), 2, 2), empirical = TRUE)
pts3 <- mvrnorm(n=100, c(0, 10), matrix(c(3, 0, 6, 3), 2, 2), empirical = TRUE)
vals <- list(pts1, pts2, pts3)
points(vals, 'set_6_3_2_eq');
pts1 <- mvrnorm(n=100, c(3, 3), matrix(c(3, 0, 6, 3), 2, 2), empirical = TRUE)
pts2 <- mvrnorm(n=100, c(7, 7), matrix(c(3, 0, 6, 3), 2, 2), empirical = TRUE)
pts3 <- mvrnorm(n=100, c(0, 10), matrix(c(3, 0, 6, 3), 2, 2), empirical = TRUE)
vals <- list(pts1, pts2, pts3)
points(vals, 'set_7_3_2_eq');
pts1 <- mvrnorm(n=100, c(4, 4), matrix(c(3, 0, 6, 3), 2, 2), empirical = TRUE)
pts2 <- mvrnorm(n=100, c(6, 6), matrix(c(3, 0, 6, 3), 2, 2), empirical = TRUE)
pts3 <- mvrnorm(n=100, c(0, 10), matrix(c(3, 0, 6, 3), 2, 2), empirical = TRUE)
vals <- list(pts1, pts2, pts3)
points(vals, 'set_8_3_2_eq');

pts1 <- mvrnorm(n=100, c(0, 0), matrix(c(3, 0, 6, 3), 2, 2), empirical = TRUE)
pts2 <- mvrnorm(n=100, c(10, 10), matrix(c(3, 0, 6, 3), 2, 2), empirical = TRUE)
pts3 <- mvrnorm(n=100, c(0, 10), matrix(c(3, 0, 6, 3), 2, 2), empirical = TRUE)
vals <- list(pts1, pts2, pts3)
points(vals, 'set_9_3_3_eq');
pts1 <- mvrnorm(n=100, c(2, 2), matrix(c(3, 0, 6, 3), 2, 2), empirical = TRUE)
pts2 <- mvrnorm(n=100, c(8, 8), matrix(c(3, 0, 6, 3), 2, 2), empirical = TRUE)
pts3 <- mvrnorm(n=100, c(2, 8), matrix(c(3, 0, 6, 3), 2, 2), empirical = TRUE)
vals <- list(pts1, pts2, pts3)
points(vals, 'set_10_3_3_eq');
pts1 <- mvrnorm(n=100, c(3, 3), matrix(c(3, 0, 6, 3), 2, 2), empirical = TRUE)
pts2 <- mvrnorm(n=100, c(7, 7), matrix(c(3, 0, 6, 3), 2, 2), empirical = TRUE)
pts3 <- mvrnorm(n=100, c(3, 7), matrix(c(3, 0, 6, 3), 2, 2), empirical = TRUE)
vals <- list(pts1, pts2, pts3)
points(vals, 'set_11_3_3_eq');
pts1 <- mvrnorm(n=100, c(4, 4), matrix(c(3, 0, 6, 3), 2, 2), empirical = TRUE)
pts2 <- mvrnorm(n=100, c(6, 6), matrix(c(3, 0, 6, 3), 2, 2), empirical = TRUE)
pts3 <- mvrnorm(n=100, c(4, 6), matrix(c(3, 0, 6, 3), 2, 2), empirical = TRUE)
vals <- list(pts1, pts2, pts3)
points(vals, 'set_12_3_3_eq');

for(j in 17:20){
	vals <- list()
	for(i in 1:8){
		pts1 <- mvrnorm(n=100, runif(2, 0, 20), matrix(c(1, 0, 2, 1), 2, 2), empirical = TRUE)
		vals[[length(vals) + 1]] <- pts1
	}
	points(vals, paste('set_',j,'_8_rand', sep=""));
}

pts1 <- mvrnorm(n=250, c(0, 0), matrix(c(3, 0, 6, 3), 2, 2), empirical = TRUE)
pts2 <- mvrnorm(n=250, c(10, 10), matrix(c(3, 0, 6, 3), 2, 2), empirical = TRUE)
vals <- list(pts1, pts2)
points(vals, 'set_500_time');
pts1 <- mvrnorm(n=500, c(0, 0), matrix(c(3, 0, 6, 3), 2, 2), empirical = TRUE)
pts2 <- mvrnorm(n=500, c(10, 10), matrix(c(3, 0, 6, 3), 2, 2), empirical = TRUE)
vals <- list(pts1, pts2)
points(vals, 'set_1000_time');
pts1 <- mvrnorm(n=750, c(0, 0), matrix(c(3, 0, 6, 3), 2, 2), empirical = TRUE)
pts2 <- mvrnorm(n=750, c(10, 10), matrix(c(3, 0, 6, 3), 2, 2), empirical = TRUE)
vals <- list(pts1, pts2)
points(vals, 'set_1500_time');
pts1 <- mvrnorm(n=1000, c(0, 0), matrix(c(3, 0, 6, 3), 2, 2), empirical = TRUE)
pts2 <- mvrnorm(n=1000, c(10, 10), matrix(c(3, 0, 6, 3), 2, 2), empirical = TRUE)
vals <- list(pts1, pts2)
points(vals, 'set_2000_time');
pts1 <- mvrnorm(n=1250, c(0, 0), matrix(c(3, 0, 6, 3), 2, 2), empirical = TRUE)
pts2 <- mvrnorm(n=1250, c(10, 10), matrix(c(3, 0, 6, 3), 2, 2), empirical = TRUE)
vals <- list(pts1, pts2)
points(vals, 'set_2500_time');
pts1 <- mvrnorm(n=1500, c(0, 0), matrix(c(3, 0, 6, 3), 2, 2), empirical = TRUE)
pts2 <- mvrnorm(n=1500, c(10, 10), matrix(c(3, 0, 6, 3), 2, 2), empirical = TRUE)
vals <- list(pts1, pts2)
points(vals, 'set_3000_time');
pts1 <- mvrnorm(n=1750, c(0, 0), matrix(c(3, 0, 6, 3), 2, 2), empirical = TRUE)
pts2 <- mvrnorm(n=1750, c(10, 10), matrix(c(3, 0, 6, 3), 2, 2), empirical = TRUE)
vals <- list(pts1, pts2)
points(vals, 'set_3500_time');
pts1 <- mvrnorm(n=2000, c(0, 0), matrix(c(3, 0, 6, 3), 2, 2), empirical = TRUE)
pts2 <- mvrnorm(n=2000, c(10, 10), matrix(c(3, 0, 6, 3), 2, 2), empirical = TRUE)
vals <- list(pts1, pts2)
points(vals, 'set_4000_time');
pts1 <- mvrnorm(n=2250, c(0, 0), matrix(c(3, 0, 6, 3), 2, 2), empirical = TRUE)
pts2 <- mvrnorm(n=2250, c(10, 10), matrix(c(3, 0, 6, 3), 2, 2), empirical = TRUE)
vals <- list(pts1, pts2)
points(vals, 'set_4500_time');
pts1 <- mvrnorm(n=2500, c(0, 0), matrix(c(3, 0, 6, 3), 2, 2), empirical = TRUE)
pts2 <- mvrnorm(n=2500, c(10, 10), matrix(c(3, 0, 6, 3), 2, 2), empirical = TRUE)
vals <- list(pts1, pts2)
points(vals, 'set_5000_time');
pts1 <- mvrnorm(n=3000, c(0, 0), matrix(c(3, 0, 6, 3), 2, 2), empirical = TRUE)
pts2 <- mvrnorm(n=3000, c(10, 10), matrix(c(3, 0, 6, 3), 2, 2), empirical = TRUE)
vals <- list(pts1, pts2)
points(vals, 'set_6000_time');
pts1 <- mvrnorm(n=3500, c(0, 0), matrix(c(3, 0, 6, 3), 2, 2), empirical = TRUE)
pts2 <- mvrnorm(n=3500, c(10, 10), matrix(c(3, 0, 6, 3), 2, 2), empirical = TRUE)
vals <- list(pts1, pts2)
points(vals, 'set_7000_time');
pts1 <- mvrnorm(n=4000, c(0, 0), matrix(c(3, 0, 6, 3), 2, 2), empirical = TRUE)
pts2 <- mvrnorm(n=4000, c(10, 10), matrix(c(3, 0, 6, 3), 2, 2), empirical = TRUE)
vals <- list(pts1, pts2)
points(vals, 'set_8000_time');
pts1 <- mvrnorm(n=4500, c(0, 0), matrix(c(3, 0, 6, 3), 2, 2), empirical = TRUE)
pts2 <- mvrnorm(n=4500, c(10, 10), matrix(c(3, 0, 6, 3), 2, 2), empirical = TRUE)
vals <- list(pts1, pts2)
points(vals, 'set_9000_time');
pts1 <- mvrnorm(n=5000, c(0, 0), matrix(c(3, 0, 6, 3), 2, 2), empirical = TRUE)
pts2 <- mvrnorm(n=5000, c(10, 10), matrix(c(3, 0, 6, 3), 2, 2), empirical = TRUE)
vals <- list(pts1, pts2)
points(vals, 'set_10000_time');


pts1 <- mvrnorm(n=250, c(4, 4), matrix(c(3, 0, 6, 3), 2, 2), empirical = TRUE)
pts2 <- mvrnorm(n=250, c(6, 6), matrix(c(3, 0, 6, 3), 2, 2), empirical = TRUE)
vals <- list(pts1, pts2)
points(vals, 'set_500_time_close');
pts1 <- mvrnorm(n=500, c(4, 4), matrix(c(3, 0, 6, 3), 2, 2), empirical = TRUE)
pts2 <- mvrnorm(n=500, c(6, 6), matrix(c(3, 0, 6, 3), 2, 2), empirical = TRUE)
vals <- list(pts1, pts2)
points(vals, 'set_1000_time_close');
pts1 <- mvrnorm(n=750, c(4, 4), matrix(c(3, 0, 6, 3), 2, 2), empirical = TRUE)
pts2 <- mvrnorm(n=750, c(6, 6), matrix(c(3, 0, 6, 3), 2, 2), empirical = TRUE)
vals <- list(pts1, pts2)
points(vals, 'set_1500_time_close');
pts1 <- mvrnorm(n=1000, c(4, 4), matrix(c(3, 0, 6, 3), 2, 2), empirical = TRUE)
pts2 <- mvrnorm(n=1000, c(6, 6), matrix(c(3, 0, 6, 3), 2, 2), empirical = TRUE)
vals <- list(pts1, pts2)
points(vals, 'set_2000_time_close');
pts1 <- mvrnorm(n=1250, c(4, 4), matrix(c(3, 0, 6, 3), 2, 2), empirical = TRUE)
pts2 <- mvrnorm(n=1250, c(6, 6), matrix(c(3, 0, 6, 3), 2, 2), empirical = TRUE)
vals <- list(pts1, pts2)
points(vals, 'set_2500_time_close');
pts1 <- mvrnorm(n=1500, c(4, 4), matrix(c(3, 0, 6, 3), 2, 2), empirical = TRUE)
pts2 <- mvrnorm(n=1500, c(6, 6), matrix(c(3, 0, 6, 3), 2, 2), empirical = TRUE)
vals <- list(pts1, pts2)
points(vals, 'set_3000_time_close');
pts1 <- mvrnorm(n=1750, c(4, 4), matrix(c(3, 0, 6, 3), 2, 2), empirical = TRUE)
pts2 <- mvrnorm(n=1750, c(6, 6), matrix(c(3, 0, 6, 3), 2, 2), empirical = TRUE)
vals <- list(pts1, pts2)
points(vals, 'set_3500_time_close');
pts1 <- mvrnorm(n=2000, c(4, 4), matrix(c(3, 0, 6, 3), 2, 2), empirical = TRUE)
pts2 <- mvrnorm(n=2000, c(6, 6), matrix(c(3, 0, 6, 3), 2, 2), empirical = TRUE)
vals <- list(pts1, pts2)
points(vals, 'set_4000_time_close');
pts1 <- mvrnorm(n=2250, c(4, 4), matrix(c(3, 0, 6, 3), 2, 2), empirical = TRUE)
pts2 <- mvrnorm(n=2250, c(6, 6), matrix(c(3, 0, 6, 3), 2, 2), empirical = TRUE)
vals <- list(pts1, pts2)
points(vals, 'set_4500_time_close');
pts1 <- mvrnorm(n=2500, c(4, 4), matrix(c(3, 0, 6, 3), 2, 2), empirical = TRUE)
pts2 <- mvrnorm(n=2500, c(6, 6), matrix(c(3, 0, 6, 3), 2, 2), empirical = TRUE)
vals <- list(pts1, pts2)
points(vals, 'set_5000_time_close');
pts1 <- mvrnorm(n=3000, c(4, 4), matrix(c(3, 0, 6, 3), 2, 2), empirical = TRUE)
pts2 <- mvrnorm(n=3000, c(6, 6), matrix(c(3, 0, 6, 3), 2, 2), empirical = TRUE)
vals <- list(pts1, pts2)
points(vals, 'set_6000_time_close');
pts1 <- mvrnorm(n=3500, c(4, 4), matrix(c(3, 0, 6, 3), 2, 2), empirical = TRUE)
pts2 <- mvrnorm(n=3500, c(6, 6), matrix(c(3, 0, 6, 3), 2, 2), empirical = TRUE)
vals <- list(pts1, pts2)
points(vals, 'set_7000_time_close');
pts1 <- mvrnorm(n=4000, c(4, 4), matrix(c(3, 0, 6, 3), 2, 2), empirical = TRUE)
pts2 <- mvrnorm(n=4000, c(6, 6), matrix(c(3, 0, 6, 3), 2, 2), empirical = TRUE)
vals <- list(pts1, pts2)
points(vals, 'set_8000_time_close');
pts1 <- mvrnorm(n=4500, c(4, 4), matrix(c(3, 0, 6, 3), 2, 2), empirical = TRUE)
pts2 <- mvrnorm(n=4500, c(6, 6), matrix(c(3, 0, 6, 3), 2, 2), empirical = TRUE)
vals <- list(pts1, pts2)
points(vals, 'set_9000_time_close');
pts1 <- mvrnorm(n=5000, c(4, 4), matrix(c(3, 0, 6, 3), 2, 2), empirical = TRUE)
pts2 <- mvrnorm(n=5000, c(6, 6), matrix(c(3, 0, 6, 3), 2, 2), empirical = TRUE)
vals <- list(pts1, pts2)
points(vals, 'set_10000_time_close');




