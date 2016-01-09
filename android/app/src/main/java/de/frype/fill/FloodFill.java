package de.frype.fill;

import android.graphics.Point;
import android.graphics.Rect;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * Created by Jan on 08.01.2016.
 */
public class FloodFill {

    private FloodFill() {}

    public static Rect simple_fill(Point2D position, byte[] mask, int[] data, int width, int height, int value) {

        // left, top and right, bottom position
        Point2D lt = new Point2D(position);
        Point2D rb = new Point2D(position);

        // create queue and add initial position
        Queue<Point2D> queue = new ArrayDeque<>();
        queue.add(position);

        // the heap loop
        while (!queue.isEmpty()) {
            // get next point
            Point2D p = queue.remove();
            int i = p.x + p.y * width;

            if (mask[i] == 1) {
                // mark as processed and set data to value
                mask[i] = 2;
                data[i] = value;

                // update lt, rb
                if (p.x < lt.x) {
                    lt.x = p.x;
                }
                if (p.x > rb.x) {
                    rb.x = p.x;
                }
                if (p.y < lt.y) {
                    lt.y = p.y;
                }
                if (p.y > rb.y) {
                    rb.y = p.y;
                }

                // up
                if (p.y > 0) {
                    queue.add(new Point2D(p.x, p.y - 1));
                }
                // down
                if (p.y < height - 1) {
                    queue.add(new Point2D(p.x, p.y + 1));
                }
                // left
                if (p.x > 0) {
                    queue.add(new Point2D(p.x - 1, p.y));
                }
                // right
                if (p.x < width - 1) {
                    queue.add(new Point2D(p.x + 1, p.y));
                }
            }
        }

        // reset mask to 1 where it is 2
        for (int y = lt.y; y <= rb.y; y++) {
            for (int x = lt.x; x <= rb.x; x++) {
                int i = x + y * width;
                if (mask[i] == 2) {
                    mask[i] = 1;
                }
            }
        }

        // bounding box
        Rect box = new Rect(lt.x, lt.y, rb.x - lt.x + 1, rb.y - lt.y + 1);
        return box;
    }

    public static Rect advanced_fill(Point2D position, byte[] mask, int[] data, int width, int height, int value) {

        // left, top and right, bottom position
        Point2D lt = new Point2D(position);
        Point2D rb = new Point2D(position);

        // create queue and add initial position
        Queue<Point2D> queue = new ArrayDeque<>();
        queue.add(position);

        // the heap loop
        while (!queue.isEmpty()) {
            // get next point
            Point2D p = queue.remove();
            int i = p.x + p.y * width;

            if (mask[i] == 1) {

                // go left until not possible anymore
                int l = 0;
                while (l < p.x && mask[i - l - 1] == 1) {
                    l++;
                }

                // go right until not possible anymore
                int r = 0;
                while (r < width - p.x - 1 && mask[i + r + 1] == 1) {
                    r++;
                }

                // go from p.x-l to p.x+r
                for (int c = -l; c <= r; c++) {

                    // mark and set value
                    mask[i + c] = 2;
                    data[i + c] = value;

                    // up
                    if (p.y > 0) {
                        queue.add(new Point2D(p.x + c, p.y - 1));
                    }
                    // down
                    if (p.y < height - 1) {
                        queue.add(new Point2D(p.x + c, p.y + 1));
                    }
                }

                // update lt, rb
                if (p.x - l < lt.x) {
                    lt.x = p.x - l;
                }
                if (p.x + r > rb.x) {
                    rb.x = p.x + r;
                }
                if (p.y < lt.y) {
                    lt.y = p.y;
                }
                if (p.y > rb.y) {
                    rb.y = p.y;
                }

            }
        }

        // reset mask to 1 where it is 2
        for (int y = lt.y; y <= rb.y; y++) {
            for (int x = lt.x; x <= rb.x; x++) {
                int i = x + y * width;
                if (mask[i] == 2) {
                    mask[i] = 1;
                }
            }
        }

        // bounding box
        Rect box = new Rect(lt.x, lt.y, rb.x - lt.x + 1, rb.y - lt.y + 1);
        return box;
    }
}
