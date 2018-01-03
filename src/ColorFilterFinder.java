import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * ֱ�Ӹ���ɫ������λ��һ�����ĵ�
 */
public class ColorFilterFinder {

    static Color bgColor = Color.RED;

    static Point startCenterPoint;

    static int lastShapeMinMax = 150;

    public static Point findEndCenter(BufferedImage bufferedImage, Point startCenterPoint) {
        ColorFilterFinder.startCenterPoint = startCenterPoint;
        bgColor = new Color(bufferedImage.getRGB(540, 700));

        Point tmpStartCenterPoint;
        Point tmpEndCenterPoint;

        // �ų�С�����ڵ�λ�õ�������״������,Ϊ���ų�ĳЩ�ض�����ĸ���.
        Rectangle rectangle = new Rectangle((int) (startCenterPoint.getX() - lastShapeMinMax / 2), 0, lastShapeMinMax,
                (int) startCenterPoint.getY());

        Color lastColor = bgColor;
        for (int y = 600; y < startCenterPoint.y; y++) {
            for (int x = 10; x < bufferedImage.getWidth(); x++) {
                if (rectangle.contains(x, y)) {
                    continue;
                }
                Color newColor = new Color(bufferedImage.getRGB(x, y));
                if ((Math.abs(newColor.getRed() - lastColor.getRed())
                        + Math.abs(newColor.getBlue() - lastColor.getBlue())
                        + Math.abs(newColor.getGreen() - lastColor.getGreen()) >= 20)
                        || (Math.abs(newColor.getRed() - lastColor.getRed()) >= 15
                                || Math.abs(newColor.getBlue() - lastColor.getBlue()) >= 15
                                || Math.abs(newColor.getGreen() - lastColor.getGreen()) >= 15)) {
                    // System.out.println(BufferImageTest.toHexFromColor(newColor));
                    // System.out.println(BufferImageTest.toHexFromColor(lastColor));
                    // System.out.println("y = " + y + " x = " + x);
                    tmpStartCenterPoint = findStartCenterPoint(bufferedImage, x, y);
                    // System.out.println(tmpStartCenterPoint);
                    tmpEndCenterPoint = findEndCenterPoint(bufferedImage, tmpStartCenterPoint);
                    return new Point(tmpStartCenterPoint.x, (tmpEndCenterPoint.y + tmpStartCenterPoint.y) / 2);
                }
            }
        }
        return null;
    }

    /**
     * �����·���/Բ����Ч�������λ��
     *
     * @param bufferedImage
     * @param tmpStartCenterPoint
     * @return
     */
    private static Point findEndCenterPoint(BufferedImage bufferedImage, Point tmpStartCenterPoint) {
        Color startColor = new Color(bufferedImage.getRGB(tmpStartCenterPoint.x, tmpStartCenterPoint.y));
        Color lastColor = startColor;
        int centX = tmpStartCenterPoint.x, centY = tmpStartCenterPoint.y;
        for (int i = tmpStartCenterPoint.y; i < bufferedImage.getHeight() && i < startCenterPoint.y - 10; i++) {
            // -2��Ϊ�˱ܿ���������ұ�ǽ�ڵ�Ӱ��
            Color newColor = new Color(bufferedImage.getRGB(tmpStartCenterPoint.x, i));
            if (Math.abs(newColor.getRed() - lastColor.getRed()) <= 8
                    && Math.abs(newColor.getGreen() - lastColor.getGreen()) <= 8
                    && Math.abs(newColor.getBlue() - lastColor.getBlue()) <= 8) {
                centY = i;
            }
        }
        if (centY - tmpStartCenterPoint.y < 40) {
            centY = centY + 40;
        }
        if (centY - tmpStartCenterPoint.y > 230) {
            centY = tmpStartCenterPoint.y + 230;
        }
        return new Point(centX, centY);
    }

    // ������һ���������ߵ���е�
    private static Point findStartCenterPoint(BufferedImage bufferedImage, int x, int y) {
        Color lastColor = new Color(bufferedImage.getRGB(x - 1, y));
        int centX = x, centY = y;
        for (int i = x; i < bufferedImage.getWidth(); i++) {
            Color newColor = new Color(bufferedImage.getRGB(i, y));
            if ((Math.abs(newColor.getRed() - lastColor.getRed()) + Math.abs(newColor.getBlue() - lastColor.getBlue())
                    + Math.abs(newColor.getGreen() - lastColor.getGreen()) >= 20)
                    || (Math.abs(newColor.getRed() - lastColor.getRed()) >= 15
                            || Math.abs(newColor.getBlue() - lastColor.getBlue()) >= 15
                            || Math.abs(newColor.getGreen() - lastColor.getGreen()) >= 15)) {
                centX = x + (i - x) / 2;
            } else {
                break;
            }
        }
        return new Point(centX, centY);
    }

    private static boolean like(Color a, Color b) {
        return !((Math.abs(a.getRed() - b.getRed()) + Math.abs(a.getBlue() - b.getBlue())
                + Math.abs(a.getGreen() - b.getGreen()) >= 20)
                || (Math.abs(a.getRed() - b.getRed()) >= 15 || Math.abs(a.getBlue() - b.getBlue()) >= 15
                        || Math.abs(a.getGreen() - b.getGreen()) >= 15));
    }

    public static void updateLastShapeMinMax(BufferedImage bufferedImage, Point first, Point second) {
        if (first.x < second.y) {
            for (int x = second.x; x < bufferedImage.getWidth(); x++) {
                Color newColor = new Color(bufferedImage.getRGB(x, second.y));
                if (like(newColor, bgColor)) {
                    lastShapeMinMax = (int) Math.max((x - second.x) * 1.5, 150);
                    break;
                }
            }
        } else {
            for (int x = second.x; x >= 10; x--) {
                Color newColor = new Color(bufferedImage.getRGB(x, second.y));
                if (like(newColor, bgColor)) {
                    lastShapeMinMax = (int) Math.max((second.x - x) * 1.5, 150);
                    break;
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {

        // BufferedImage bufferedImage = ImageIO.read(new
        // File(Constants.SCREENSHOT_2));
        BufferedImage bufferedImage = ImageIO.read(new File("/Users/tangshuai/Desktop/tmp/665_908.png"));
        Point point = StartCenterFinder.findStartCenter(bufferedImage);
        System.out.println(point);

        Point point2 = findEndCenter(bufferedImage, point);
        System.out.println(point2);

    }

}
