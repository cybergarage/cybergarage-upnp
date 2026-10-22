package org.cybergarage.upnp.std.av.controller;

import static org.junit.Assert.*;
import java.util.*;
import org.cybergarage.upnp.Device;
import org.cybergarage.upnp.std.av.server.object.container.ContainerNode;
import org.cybergarage.xml.Node;
import org.junit.Test;

public class MediaBrowseSecurityTest {
  private static class Controller extends MediaController {
    final List<String> calls = new ArrayList<>();
    int mode;
    int metadata;

    public Node browseMetaData(Device dev, String id, String f, int s, int count, String sort) {
      metadata++;
      return null;
    }

    public Node browseDirectChildren(
        Device dev, String id, String f, int s, int count, String sort) {
      calls.add(id);
      Node result = new Node("DIDL-Lite");
      if (mode == 0 && !id.equals("0")) return result;
      Node child = new Node("container");
      child.setAttribute(
          "id",
          mode == 1
              ? id
              : mode == 2
                  ? Integer.toString(Integer.parseInt(id) + 1)
                  : mode == 3 ? (id.equals("0") ? "1" : "0") : "1");
      child.setAttribute("childCount", 1);
      result.addNode(child);
      return result;
    }
  }

  @Test
  public void preservesFiniteTreesAndIndependentFlags() {
    for (boolean recursive : new boolean[] {false, true})
      for (boolean metadata : new boolean[] {false, true}) {
        Controller c = new Controller();
        ContainerNode root = c.browse(null, "0", recursive, metadata);
        assertEquals(1, root.getNContentNodes());
        assertEquals(recursive ? Arrays.asList("0", "1") : Arrays.asList("0"), c.calls);
        assertEquals(metadata ? 1 : 0, c.metadata);
      }
  }

  @Test(timeout = 1000)
  public void rejectsSelfCycleAndDeepUniqueChains() {
    for (int mode : new int[] {1, 2, 3}) {
      Controller c = new Controller();
      c.mode = mode;
      assertThrows(IllegalArgumentException.class, () -> c.browse(null, "0", true, false));
      assertTrue(c.calls.size() <= 32);
    }
  }
}
