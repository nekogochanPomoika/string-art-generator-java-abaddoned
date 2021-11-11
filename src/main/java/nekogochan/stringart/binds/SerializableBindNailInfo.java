package nekogochan.stringart.binds;

import nekogochan.stringart.nail.Nail;
import nekogochan.stringart.nail.NailImpl;
import nekogochan.stringart.point.RectPoint;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public record SerializableBindNailInfo(NailInfo nailInfo,
                                       NailInfoList leftToLeft,
                                       NailInfoList leftToRight,
                                       NailInfoList rightToLeft,
                                       NailInfoList rightToRight) implements Serializable {

  public static record NailInfo(double x, double y, double radius) implements Serializable {}

  public static SerializableBindNailInfo fromBindNail(BindNail nail) {
    return new SerializableBindNailInfo(nailInfoFromNail(nail),
                                        new NailInfoList(nail.leftToLeft()),
                                        new NailInfoList(nail.leftToRight()),
                                        new NailInfoList(nail.rightToLeft()),
                                        new NailInfoList(nail.rightToRight()));
  }

  public StandardBindNail toStandardBindNail() {
    var nail = new StandardBindNail(new NailImpl(new RectPoint(nailInfo.x,
                                                               nailInfo.y),
                                                 nailInfo.radius));

    nail.bind(leftToLeft.toBindNail(),
              leftToRight.toBindNail(),
              rightToLeft.toBindNail(),
              rightToRight.toBindNail());

    return nail;
  }

  private static NailInfo nailInfoFromNail(Nail nail) {
    return new NailInfo(nail.center().x(),
                        nail.center().y(),
                        nail.radius());
  }

  private static class NailInfoList extends ArrayList<NailInfo> implements Serializable {
    public NailInfoList(Iterable<? extends Nail> nails) {
      super(StreamSupport.stream(nails.spliterator(), false)
                         .map(SerializableBindNailInfo::nailInfoFromNail)
                         .collect(Collectors.toCollection(ArrayList::new)));
    }

    public List<? extends BindNail> toBindNail() {
      return this.stream()
                 .map(i -> new StandardBindNail(new NailImpl(new RectPoint(i.x, i.y), i.radius)))
                 .toList();
    }
  }
}
