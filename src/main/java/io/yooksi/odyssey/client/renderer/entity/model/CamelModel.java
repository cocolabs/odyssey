package io.yooksi.odyssey.client.renderer.entity.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import io.yooksi.odyssey.entity.passive.CamelEntity;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

@OnlyIn(Dist.CLIENT)
public class CamelModel
    extends EntityModel<CamelEntity> {

  private final ModelRenderer one;
  private final ModelRenderer two;
  private final ModelRenderer bb_main;
  private final ModelRenderer front_leg_right;
  private final ModelRenderer front_leg_right_top;
  private final ModelRenderer front_leg_right_bottom;
  private final ModelRenderer front_leg_left_foot;
  private final ModelRenderer front_leg_left;
  private final ModelRenderer front_leg_left_top;
  private final ModelRenderer front_leg_left_bottom;
  private final ModelRenderer front_leg_right_foot;
  private final ModelRenderer back_leg_right;
  private final ModelRenderer back_leg_right_top;
  private final ModelRenderer back_leg_right_bottom;
  private final ModelRenderer back_leg_right_foot;
  private final ModelRenderer back_leg_left;
  private final ModelRenderer back_leg_left_top;
  private final ModelRenderer back_leg_left_bottom;
  private final ModelRenderer back_leg_left_foot;
  private final ModelRenderer neck;
  private final ModelRenderer neck_base;
  private final ModelRenderer neck_top;
  private final ModelRenderer head;
  private final ModelRenderer ears;
  private final ModelRenderer ear_left;
  private final ModelRenderer ear_right;
  private final ModelRenderer tail;
  private final ModelRenderer hump;
  private final ModelRenderer hump_back;
  private final ModelRenderer hump_front;
  private final ModelRenderer hump_butt;
  private final ModelRenderer hump_back_1;

  public CamelModel() {

    super();

    /*
       1.12.2
       ModelBox params:
         ModelBox(renderer, texU, texV, x, y, z, dx, dy, dz, delta, mirror)
       1.15.2
       ModelBox params, same as:
         ModelRenderer#addBox(texU, texV, x, y, z, dx, dy, dz, deltaX, deltaY, deltaZ, mirror, texWidth, texHeight)
         ModelRenderer#addBox(x, y, z, dx, dy, dz)
         ModelRenderer#addBox(x, y, z, dx, dy, dz, mirror)
         ModelRenderer#addBox(x, y, z, dx, dy, dz, delta)
         ModelRenderer#addBox(x, y, z, dx, dy, dz, deltaX, deltaY, deltaZ)
         ModelRenderer#addBox(x, y, z, dx, dy, dz, delta, mirror)
     */

    this.textureWidth = 128;
    this.textureHeight = 128;

    bb_main = new ModelRenderer(this);
    bb_main.setRotationPoint(0.0F, 24.0F, 0.0F);
    bb_main.setTextureOffset(3, 2)
        .addBox(-5.0F, -22.45F, -10.0F, 13, 8, 20, 0.0F, false);

    front_leg_right = new ModelRenderer(this);
    front_leg_right.setRotationPoint(8.725F, 6.125F, -8.5F);
    setRotationAngle(front_leg_right, 0.7854F, 0.0F, 0.0F);

    front_leg_right_top = new ModelRenderer(this);
    front_leg_right_top.setRotationPoint(-4.725F, 0.2048F, -0.1406F);
    setRotationAngle(front_leg_right_top, -0.7854F, 0.0F, 0.0F);
    front_leg_right.addChild(front_leg_right_top);
    front_leg_right_top.setTextureOffset(66, 26)
        .addBox(-0.375F, -1.3531F, -2.1361F, 5, 9, 4, 0.0F, false);

    front_leg_right_bottom = new ModelRenderer(this);
    front_leg_right_bottom.setRotationPoint(4.0F, 8.1F, 0.0F);
    front_leg_right_top.addChild(front_leg_right_bottom);
    front_leg_right_bottom.setTextureOffset(29, 77)
        .addBox(-3.975F, -1.1281F, -1.5361F, 4, 10, 3, 0.0F, false);

    front_leg_left_foot = new ModelRenderer(this);
    front_leg_left_foot.setRotationPoint(0.0F, 9.258F, 0.1786F);
    front_leg_right_bottom.addChild(front_leg_left_foot);
    front_leg_left_foot.setTextureOffset(45, 82)
        .addBox(-4.0F, -1.618F, -2.0542F, 4, 2, 4, 0.0F, false);

    front_leg_left = new ModelRenderer(this);
    front_leg_left.setRotationPoint(-5.0F, 6.125F, -8.5F);
    setRotationAngle(front_leg_left, 0.7854F, 0.0F, 0.0F);

    front_leg_left_top = new ModelRenderer(this);
    front_leg_left_top.setRotationPoint(0.0F, 0.2048F, -0.1406F);
    setRotationAngle(front_leg_left_top, -0.7854F, 0.0F, 0.0F);
    front_leg_left.addChild(front_leg_left_top);
    front_leg_left_top.setTextureOffset(45, 65)
        .addBox(-0.5F, -1.2781F, -2.0361F, 5, 9, 4, 0.0F, false);

    front_leg_left_bottom = new ModelRenderer(this);
    front_leg_left_bottom.setRotationPoint(0.0F, 9.1F, -1.0F);
    front_leg_left_top.addChild(front_leg_left_bottom);
    front_leg_left_bottom.setTextureOffset(15, 76)
        .addBox(-0.025F, -2.1281F, -0.5361F, 4, 10, 3, 0.0F, false);

    front_leg_right_foot = new ModelRenderer(this);
    front_leg_right_foot.setRotationPoint(0.0F, 8.258F, 1.1786F);
    front_leg_left_bottom.addChild(front_leg_right_foot);
    front_leg_right_foot.setTextureOffset(45, 82)
        .addBox(0.0F, -1.618F, -2.1042F, 4, 2, 4, 0.0F, false);

    back_leg_right = new ModelRenderer(this);
    back_leg_right.setRotationPoint(-5.0F, 15.4298F, -9.6406F);

    back_leg_right_top = new ModelRenderer(this);
    back_leg_right_top.setRotationPoint(0.0F, 0.0F, 0.0F);
    back_leg_right.addChild(back_leg_right_top);
    back_leg_right_top.setTextureOffset(27, 62)
        .addBox(9.675F, -11.5781F, 14.9639F, 4, 10, 6, 0.0F, false);

    back_leg_right_bottom = new ModelRenderer(this);
    back_leg_right_bottom.setRotationPoint(0.0F, 0.0F, 0.0F);
    back_leg_right_top.addChild(back_leg_right_bottom);
    back_leg_right_bottom.setTextureOffset(63, 69)
        .addBox(9.4F, -2.2531F, 15.9639F, 4, 10, 3, 0.0F, false);

    back_leg_right_foot = new ModelRenderer(this);
    back_leg_right_foot.setRotationPoint(5.0F, 8.5702F, 9.6406F);
    back_leg_right_bottom.addChild(back_leg_right_foot);
    back_leg_right_foot.setTextureOffset(45, 82)
        .addBox(4.35F, -2.1801F, 6.1588F, 4, 2, 4, 0.0F, false);

    back_leg_left = new ModelRenderer(this);
    back_leg_left.setRotationPoint(0.0F, 24.0F, 0.0F);

    back_leg_left_top = new ModelRenderer(this);
    back_leg_left_top.setRotationPoint(-5.0F, -9.0702F, -10.1406F);
    back_leg_left.addChild(back_leg_left_top);
    back_leg_left_top.setTextureOffset(0, 63)
        .addBox(-0.825F, -11.0281F, 15.2639F, 4, 10, 6, 0.0F, false);

    back_leg_left_bottom = new ModelRenderer(this);
    back_leg_left_bottom.setRotationPoint(0.0F, 0.0F, 0.0F);
    back_leg_left_top.addChild(back_leg_left_bottom);
    back_leg_left_bottom.setTextureOffset(75, 40)
        .addBox(-0.6F, -2.1281F, 16.4389F, 4, 10, 3, 0.0F, false);

    back_leg_left_foot = new ModelRenderer(this);
    back_leg_left_foot.setRotationPoint(5.0F, 8.5702F, 9.6406F);
    back_leg_left_bottom.addChild(back_leg_left_foot);
    back_leg_left_foot.setTextureOffset(45, 82)
        .addBox(-5.65F, -1.6801F, 6.5088F, 4, 2, 4, 0.0F, false);

    neck = new ModelRenderer(this);
    neck.setRotationPoint(0.0F, 24.0F, 0.0F);

    neck_base = new ModelRenderer(this);
    neck_base.setRotationPoint(0.0F, -21.0F, -8.0F);
    neck.addChild(neck_base);
    neck_base.setTextureOffset(48, 0)
        .addBox(-2.0F, -3.0F, -7.75F, 7, 7, 9, 0.0F, false);

    neck_top = new ModelRenderer(this);
    neck_top.setRotationPoint(0.0F, 0.0F, -6.0F);
    neck_base.addChild(neck_top);
    neck_top.setTextureOffset(0, 0)
        .addBox(-1.0F, -11.75F, -3.0F, 5, 15, 4, 0.0F, false);

    head = new ModelRenderer(this);
    head.setRotationPoint(1.0033F, -11.75F, -1.8167F);
    neck_top.addChild(head);
    head.setTextureOffset(17, 51)
        .addBox(-1.9933F, -3.0F, -4.2833F, 5, 6, 7, 0.0F, false);
    head.setTextureOffset(8, 55)
        .addBox(-1.0033F, -2.25F, -8.0333F, 3, 5, 4, 0.0F, false);

    ears = new ModelRenderer(this);
    ears.setRotationPoint(-1.0033F, 31.0F, 17.3667F);
    head.addChild(ears);

    ear_left = new ModelRenderer(this);
    ear_left.setRotationPoint(0.0F, -34.0F, -15.75F);
    setRotationAngle(ear_left, 0.0F, -0.6109F, -0.2618F);
    ears.addChild(ear_left);
    ear_left.setTextureOffset(0, 47)
        .addBox(-1.3864F, -1.5338F, -0.0915F, 2, 3, 2, 0.0F, false);

    ear_right = new ModelRenderer(this);
    ear_right.setRotationPoint(3.0F, -34.0F, -15.75F);
    setRotationAngle(ear_right, 0.0F, 0.5236F, 0.2618F);
    ears.addChild(ear_right);
    ear_right.setTextureOffset(0, 30)
        .addBox(-0.6666F, -1.7753F, -0.1286F, 2, 3, 2, 0.0F, false);

    tail = new ModelRenderer(this);
    tail.setRotationPoint(1.4F, 8.65F, 12.225F);
    setRotationAngle(tail, 0.0873F, 0.0F, 0.0F);
    tail.setTextureOffset(92, 55)
        .addBox(-1.0F, -3.7672F, -1.1117F, 2, 5, 1, 0.0F, false);

    hump = new ModelRenderer(this);
    hump.setRotationPoint(0.0F, 23.8F, 1.75F);
    hump.setTextureOffset(0, 31)
        .addBox(-4.01F, -25.6908F, -7.5432F, 11, 6, 10, 0.0F, false);

    hump_back = new ModelRenderer(this);
    hump_back.setRotationPoint(0.0F, -21.95F, 3.25F);
    setRotationAngle(hump_back, -0.6981F, 0.0F, 0.0F);
    hump.addChild(hump_back);

    hump_front = new ModelRenderer(this);
    hump_front.setRotationPoint(0.0F, -27.025F, -9.875F);
    setRotationAngle(hump_front, 0.9599F, 0.0F, 0.0F);
    hump.addChild(hump_front);
    hump_front.setTextureOffset(59, 59)
        .addBox(-4.0F, 2.6561F, -4.7345F, 11, 5, 5, 0.0F, false);

    hump_butt = new ModelRenderer(this);
    hump_butt.setRotationPoint(-0.01F, -21.9544F, 3.1792F);
    setRotationAngle(hump_butt, -1.2217F, 0.0F, 0.0F);
    hump.addChild(hump_butt);
    hump_butt.setTextureOffset(54, 24)
        .addBox(-3.98F, -4.6033F, 0.6212F, 11, 5, 4, 0.0F, false);

    hump_back_1 = new ModelRenderer(this);
    hump_back_1.setRotationPoint(0.0F, -21.95F, 3.25F);
    setRotationAngle(hump_back_1, -0.6981F, 0.0F, 0.0F);
    hump.addChild(hump_back_1);

    one = new ModelRenderer(this);
    one.setRotationPoint(-0.035F, -9.9649F, -2.5906F);
    setRotationAngle(one, 1.0777F, 0.0F, 0.0F);
    hump_back_1.addChild(one);
    one.setTextureOffset(31, 52)
        .addBox(-3.955F, 0.2033F, -14.4684F, 11, 5, 6, 0.0F, false);

    two = new ModelRenderer(this);
    two.setRotationPoint(0.0F, 0.0F, 0.0F);
    setRotationAngle(two, 0.0873F, 0.0F, 0.0F);
    hump_back.addChild(two);
    two.setTextureOffset(35, 38)
        .addBox(-4.0F, -3.3774F, -5.9113F, 11, 5, 9, 0.0F, false);
  }

  public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {

    modelRenderer.rotateAngleX = x;
    modelRenderer.rotateAngleY = y;
    modelRenderer.rotateAngleZ = z;
  }

  @Override
  public void setRotationAngles(@Nonnull CamelEntity camelEntity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    this.head.rotateAngleX = headPitch * ((float)Math.PI / 180F);
    this.head.rotateAngleY = netHeadYaw * ((float)Math.PI / 180F);
  }

  @Override
  public void render(@Nonnull MatrixStack matrixStack, @Nonnull IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {

    this.bb_main.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    this.front_leg_right.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    this.front_leg_left.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    this.back_leg_right.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    this.back_leg_left.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    this.neck.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    this.tail.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    this.hump.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
  }
}
