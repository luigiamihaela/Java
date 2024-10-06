import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

class Point {
    double x, y;

    Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double distanceTo(Point other) {
        return Math.sqrt(Math.pow((other.x - this.x), 2) + Math.pow((other.y - this.y), 2));
    }
}

class Shape {
    ArrayList<Point> points;
    ArrayList<Double> distances;

    Shape() {
        points = new ArrayList<>();
        distances = new ArrayList<>();
    }

    public void addPoints(Point point) {
        points.add(point);
    }

    public void calculateDistances() {
        for (int i = 0; i < points.size(); i++) {
            distances.add(points.get(i).distanceTo((points.get((i + 1) % points.size()))));
        }

    }

    public double getPerimeter() {
        calculateDistances();
        double perimeter = 0;
        for (int i = 0; i < distances.size(); i++) {
            perimeter += distances.get(i);
        }
        return perimeter;
    }

    public double getLargestSide() {
        double maxi = 0;
        for (int i = 0; i < distances.size(); i++) {
            if (distances.get(i) > maxi) {
                maxi = distances.get(i);
            }
        }
        return maxi;
    }

    public double getLargestX() {
        double maxi = Double.NEGATIVE_INFINITY;
        for (Point i : points) {
            if (i.x > maxi) {
                maxi = i.x;
            }
        }
        return maxi;
    }

    public double averageLenght() {
        return getPerimeter() / distances.size();
    }
}

public class PerimeterApp {
    private static ArrayList<Shape> shapes = new ArrayList<>();
    private static String fileWithLargestPerimeter = null;
    private static double largestPerimeter = 0;

    public static void main(String[] args) {
        String[] filenames = { "Java/Perimeter/shape1.txt", "Java/Perimeter/shape2.txt", "Java/Perimeter/shape3.txt" };
        for (String string : filenames) {
            System.out.println("Current file: " + string);
            Shape shape = new Shape();

            try (BufferedReader br = new BufferedReader(new FileReader(string))) {
                String line;
                int numPoints = Integer.parseInt(br.readLine().trim());

                for (int i = 0; i < numPoints; i++) {
                    line = br.readLine();
                    if (line != null) {
                        String[] parts = line.trim().split("\\s+");
                        double x = Double.parseDouble(parts[0]);
                        double y = Double.parseDouble(parts[1]);
                        shape.addPoints(new Point(x, y));
                    }
                }
                shapes.add(shape);
                double perimeter = shape.getPerimeter();
                System.out.println("Perimeter of file nr "+shapes.size()+" is: "+ perimeter);
                System.out.println("Largest side of file nr "+shapes.size()+" is: "+shape.getLargestSide());
                System.out.println("Largest X of file nr "+shapes.size()+" is: "+shape.getLargestX());
                System.out.println("Average lenght of file nr "+shapes.size()+" is: "+shape.averageLenght());
                if (perimeter > largestPerimeter) {
                    largestPerimeter = perimeter;
                    fileWithLargestPerimeter = string;
                }
            }
            catch (IOException e){
                System.err.println("Error reading the fle "+string+":"+e.getMessage());
            }
            System.out.println();
        } 
    if (fileWithLargestPerimeter != null){
        System.out.println("File with the largest perimeter: "+fileWithLargestPerimeter);
        System.out.println("Largest perimeter: "+largestPerimeter);
    } else{
        System.out.println("No files were processed.");
    }
    }
}