package app.tabser.rendering.geometry;
//
//public final class ViewPort {
//    public float deltaX;
//    public float deltaY;
//    public final Rectangle area = new Rectangle();
//
//    public Rectangle translate(Rectangle original) {
//        return new Rectangle((int) (original.left + deltaX), (int) (original.top + deltaY),
//                (int) (original.right + deltaX), (int) (original.bottom + deltaY));
//    }
//
//    public boolean isInView(Rectangle blockBoundsOnPage) {
//        return translate(blockBoundsOnPage).intersect(area);
//    }
//}
