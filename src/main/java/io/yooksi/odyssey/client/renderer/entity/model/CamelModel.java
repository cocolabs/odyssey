package io.yooksi.odyssey.client.renderer.entity.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import io.yooksi.odyssey.entity.passive.CamelEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

import static io.yooksi.odyssey.entity.passive.CamelEntity.SIT_TIMER_START_VALUE;

@OnlyIn(Dist.CLIENT)
public class CamelModel
    extends EntityModel<CamelEntity> {

  private static final float PI = (float) Math.PI;
  private static final float HALF_PI = (float) (Math.PI * 0.5f);
  private static final float DEGREES_TO_RADIANS = (float) (Math.PI / 180.0);

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
  private final ModelRenderer hump_back_middle;
  private final ModelRenderer body;

  /**
   * Used to store a value [0,1] that is passed from the setup method into the
   * render method and used to control the entity's rendered Y position.
   */
  private float sitScalar;

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

    this.front_leg_right = new ModelRenderer(this);
    this.front_leg_right.setRotationPoint(8.725F, 6.125F, -8.5F);

    this.front_leg_right_top = new ModelRenderer(this);
    this.front_leg_right_top.setRotationPoint(-4.725F, 0.2048F, -0.1406F);
    this.front_leg_right.addChild(this.front_leg_right_top);
    this.front_leg_right_top.setTextureOffset(66, 26).addBox(-1.875F, -1.3531F, -2.1361F, 5.0F, 9.0F, 4.0F, 0.0F, false);

    this.front_leg_right_bottom = new ModelRenderer(this);
    this.front_leg_right_bottom.setRotationPoint(4.0F, 7.1F, 0.0F);
    this.front_leg_right_top.addChild(this.front_leg_right_bottom);
    this.front_leg_right_bottom.setTextureOffset(29, 77).addBox(-5.475F, -0.1281F, -1.5361F, 4.0F, 10.0F, 3.0F, 0.0F, false);

    this.front_leg_left_foot = new ModelRenderer(this);
    this.front_leg_left_foot.setRotationPoint(0.0F, 10.258F, 0.1786F);
    this.front_leg_right_bottom.addChild(this.front_leg_left_foot);
    this.front_leg_left_foot.setTextureOffset(45, 82).addBox(-5.5F, -1.618F, -2.0542F, 4.0F, 2.0F, 4.0F, 0.0F, false);

    this.front_leg_left = new ModelRenderer(this);
    this.front_leg_left.setRotationPoint(-5.0F, 6.125F, -8.5F);

    this.front_leg_left_top = new ModelRenderer(this);
    this.front_leg_left_top.setRotationPoint(0.0F, 0.2048F, -0.1406F);
    this.front_leg_left.addChild(this.front_leg_left_top);
    this.front_leg_left_top.setTextureOffset(45, 65).addBox(-2.0F, -1.2781F, -2.0361F, 5.0F, 9.0F, 4.0F, 0.0F, false);

    this.front_leg_left_bottom = new ModelRenderer(this);
    this.front_leg_left_bottom.setRotationPoint(0.0F, 7.1F, 0.0F);
    this.front_leg_left_top.addChild(this.front_leg_left_bottom);
    this.front_leg_left_bottom.setTextureOffset(15, 76).addBox(-1.525F, -0.1281F, -1.5361F, 4.0F, 10.0F, 3.0F, 0.0F, false);

    this.front_leg_right_foot = new ModelRenderer(this);
    this.front_leg_right_foot.setRotationPoint(0.0F, 10.258F, 0.1786F);
    this.front_leg_left_bottom.addChild(this.front_leg_right_foot);
    this.front_leg_right_foot.setTextureOffset(45, 82).addBox(-1.5F, -1.618F, -2.1042F, 4.0F, 2.0F, 4.0F, 0.0F, false);

    this.back_leg_right = new ModelRenderer(this);
    this.back_leg_right.setRotationPoint(8.5F, 6.125F, 8.3594F);

    this.back_leg_right_top = new ModelRenderer(this);
    this.back_leg_right_top.setRotationPoint(-13.5F, 9.3048F, -18.0F);
    this.back_leg_right.addChild(this.back_leg_right_top);
    this.back_leg_right_top.setTextureOffset(27, 62).addBox(8.175F, -11.5781F, 14.9639F, 4.0F, 10.0F, 6.0F, 0.0F, false);

    this.back_leg_right_bottom = new ModelRenderer(this);
    this.back_leg_right_bottom.setRotationPoint(13.5F, -2.4298F, 17.3906F);
    this.back_leg_right_top.addChild(this.back_leg_right_bottom);
    this.back_leg_right_bottom.setTextureOffset(63, 69).addBox(-5.6F, 0.1767F, -1.4267F, 4.0F, 10.0F, 3.0F, 0.0F, false);

    this.back_leg_right_foot = new ModelRenderer(this);
    this.back_leg_right_foot.setRotationPoint(-8.5F, 11.0F, -7.75F);
    this.back_leg_right_bottom.addChild(this.back_leg_right_foot);
    this.back_leg_right_foot.setTextureOffset(45, 82).addBox(2.85F, -2.1801F, 5.9838F, 4.0F, 2.0F, 4.0F, 0.0F, false);

    this.back_leg_left = new ModelRenderer(this);
    this.back_leg_left.setRotationPoint(-5.5F, 6.125F, 8.3594F);

    this.back_leg_left_top = new ModelRenderer(this);
    this.back_leg_left_top.setRotationPoint(0.5F, 8.8048F, -18.5F);
    this.back_leg_left.addChild(this.back_leg_left_top);
    this.back_leg_left_top.setTextureOffset(0, 63).addBox(-2.325F, -11.0281F, 15.2639F, 4.0F, 10.0F, 6.0F, 0.0F, false);

    this.back_leg_left_bottom = new ModelRenderer(this);
    this.back_leg_left_bottom.setRotationPoint(1.5F, -1.9298F, 17.8906F);
    this.back_leg_left_top.addChild(this.back_leg_left_bottom);
    this.back_leg_left_bottom.setTextureOffset(75, 40).addBox(-3.6F, -0.1983F, -1.4517F, 4.0F, 10.0F, 3.0F, 0.0F, false);

    this.back_leg_left_foot = new ModelRenderer(this);
    this.back_leg_left_foot.setRotationPoint(3.5F, 10.5F, -8.25F);
    this.back_leg_left_bottom.addChild(this.back_leg_left_foot);
    this.back_leg_left_foot.setTextureOffset(45, 82).addBox(-7.15F, -1.6801F, 6.4338F, 4.0F, 2.0F, 4.0F, 0.0F, false);

    this.neck = new ModelRenderer(this);
    this.neck.setRotationPoint(0.0F, 24.0F, 0.0F);

    this.neck_base = new ModelRenderer(this);
    this.neck_base.setRotationPoint(0.0F, -21.0F, -8.0F);
    this.neck.addChild(this.neck_base);
    this.neck_base.setTextureOffset(48, 0).addBox(-3.5F, -3.0F, -7.75F, 7.0F, 7.0F, 9.0F, 0.0F, false);

    this.neck_top = new ModelRenderer(this);
    this.neck_top.setRotationPoint(0.0F, 0.0F, -6.0F);
    this.neck_base.addChild(this.neck_top);
    this.neck_top.setTextureOffset(0, 0).addBox(-2.5F, -11.75F, -3.0F, 5.0F, 15.0F, 4.0F, 0.0F, false);

    this.head = new ModelRenderer(this);
    this.head.setRotationPoint(1.0033F, -11.75F, -1.8167F);
    this.neck_top.addChild(this.head);
    this.head.setTextureOffset(17, 51).addBox(-3.4933F, -3.0F, -4.2833F, 5.0F, 6.0F, 7.0F, 0.0F, false);
    this.head.setTextureOffset(8, 55).addBox(-2.5033F, -2.25F, -8.0333F, 3.0F, 5.0F, 4.0F, 0.0F, false);

    this.ears = new ModelRenderer(this);
    this.ears.setRotationPoint(-1.0033F, 31.0F, 17.3667F);
    this.head.addChild(this.ears);

    this.ear_left = new ModelRenderer(this);
    this.ear_left.setRotationPoint(-0.5F, -35.0F, -14.0F);
    this.ears.addChild(this.ear_left);
    this.setRotationAngle(this.ear_left, 0.0F, -0.6109F, -0.2618F);
    this.ear_left.setTextureOffset(0, 47).addBox(-2.8864F, -1.5338F, -0.0915F, 2.0F, 3.0F, 2.0F, 0.0F, false);

    this.ear_right = new ModelRenderer(this);
    this.ear_right.setRotationPoint(3.0F, -34.0F, -15.75F);
    this.ears.addChild(this.ear_right);
    this.setRotationAngle(this.ear_right, 0.0F, 0.5236F, 0.2618F);
    this.ear_right.setTextureOffset(0, 30).addBox(-2.1666F, -1.7753F, -0.1286F, 2.0F, 3.0F, 2.0F, 0.0F, false);

    this.tail = new ModelRenderer(this);
    this.tail.setRotationPoint(1.4F, 8.65F, 12.225F);
    setRotationAngle(this.tail, 0.0873F, 0.0F, 0.0F);
    this.tail.setTextureOffset(92, 55).addBox(-2.5F, -3.7672F, -1.1117F, 2.0F, 5.0F, 1.0F, 0.0F, false);

    this.hump = new ModelRenderer(this);
    this.hump.setRotationPoint(0.0F, 23.8F, 1.75F);
    this.hump.setTextureOffset(0, 31).addBox(-5.51F, -25.719F, -7.547F, 11.0F, 6.0F, 10.0F, 0.0F, false);

    this.hump_back = new ModelRenderer(this);
    this.hump_back.setRotationPoint(0.0F, -21.95F, 3.25F);
    this.hump.addChild(this.hump_back);
    this.setRotationAngle(this.hump_back, -0.6109F, 0.0F, 0.0F);
    this.hump_back.setTextureOffset(35, 38).addBox(-5.5F, -3.3774F, -5.9113F, 11.0F, 5.0F, 9.0F, 0.0F, false);

    this.hump_front = new ModelRenderer(this);
    this.hump_front.setRotationPoint(0.0F, -27.025F, -9.875F);
    this.hump.addChild(this.hump_front);
    this.setRotationAngle(this.hump_front, 0.9599F, 0.0F, 0.0F);
    this.hump_front.setTextureOffset(59, 59).addBox(-5.5F, 2.6561F, -4.7345F, 11.0F, 5.0F, 5.0F, 0.0F, false);

    this.hump_butt = new ModelRenderer(this);
    this.hump_butt.setRotationPoint(-0.01F, -21.9544F, 3.1792F);
    this.hump.addChild(this.hump_butt);
    this.setRotationAngle(this.hump_butt, -1.2217F, 0.0F, 0.0F);
    this.hump_butt.setTextureOffset(54, 24).addBox(-5.48F, -4.6033F, 0.6212F, 11.0F, 5.0F, 4.0F, 0.0F, false);

    this.hump_back_middle = new ModelRenderer(this);
    this.hump_back_middle.setRotationPoint(1.51F, -24.5866F, -1.7901F);
    this.hump.addChild(this.hump_back_middle);
    this.setRotationAngle(this.hump_back_middle, 0.3883F, 0.0F, 0.0F);
    this.hump_back_middle.setTextureOffset(31, 52).addBox(-7.0F, -2.4501F, -3.6907F, 11.0F, 5.0F, 7.0F, 0.0F, false);

    this.body = new ModelRenderer(this);
    this.body.setRotationPoint(0.0F, 8.55F, 0.0F);
    this.body.setTextureOffset(3, 2).addBox(-6.5F, -7.0F, -10.0F, 13.0F, 8.0F, 20.0F, 0.0F, false);
  }

  public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {

    modelRenderer.rotateAngleX = x;
    modelRenderer.rotateAngleY = y;
    modelRenderer.rotateAngleZ = z;
  }

  @Override
  public void setLivingAnimations(CamelEntity camelEntity, float limbSwing, float limbSwingAmount, float partialTick) {

    // -------------------------------------------------------------------------
    // - Eat / Drink Animation
    // -------------------------------------------------------------------------

    // Smooth the animation by applying the partial ticks. The eat drink timer
    // only ever moves in one direction, so we don't need to worry about the
    // sign of the partial ticks.
    float eatDrinkTimer = camelEntity.getEatDrinkTimer() - partialTick;

    if (eatDrinkTimer > 0) {
      this.neck_top.rotateAngleY = 0;

      float scalar = 1;

      if (eatDrinkTimer > 40) {
        scalar = 0;

      } else if (eatDrinkTimer > 30) {
        scalar = 1 - ((eatDrinkTimer - 30) / 10f);

      } else if (eatDrinkTimer <= 10) {
        scalar = (eatDrinkTimer / 10f);
      }

      this.neck_base.rotateAngleX = 15 * DEGREES_TO_RADIANS * scalar;
      this.neck_top.rotateAngleX = (90 + 45) * DEGREES_TO_RADIANS * scalar;
      this.head.rotateAngleX = -75 * DEGREES_TO_RADIANS * scalar;

    } else {
      this.neck_base.rotateAngleX = 0;
      this.neck_top.rotateAngleX = 0;
    }

    // -------------------------------------------------------------------------
    // - Sit / Stand
    // -------------------------------------------------------------------------

    if ((camelEntity.isSitting() && camelEntity.getSitTimer() >= 0)
        || (!camelEntity.isSitting() && camelEntity.getSitTimer() < SIT_TIMER_START_VALUE)) {

      // This will smooth the sitting and standing animations by either adding
      // or subtracting the partial tick depending on the direction of the move.
      if ((camelEntity.isSitting() && camelEntity.getSitTimer() >= 0)) {
        this.sitScalar = MathHelper.clamp(1 - (camelEntity.getSitTimer() - partialTick) / (float) SIT_TIMER_START_VALUE, 0, 1);

      } else {
        this.sitScalar = MathHelper.clamp(1 - (camelEntity.getSitTimer() + partialTick) / (float) SIT_TIMER_START_VALUE, 0, 1);
      }

      // Front Leg
      {
        this.front_leg_right.rotateAngleX = (float) (-Math.PI * 0.5f + (15 * DEGREES_TO_RADIANS)) * this.sitScalar;
        this.front_leg_left.rotateAngleX = (float) (-Math.PI * 0.5f + (15 * DEGREES_TO_RADIANS)) * this.sitScalar;
      }

      // Front Leg Bottom
      {
        // In this one, we swing the bottom legs out a little bit on the Z axis
        // to avoid some Z-fighting, plus it looks better to be able to see that
        // part of the leg.
        this.front_leg_right_bottom.rotateAngleX = (float) (Math.PI - (25 * DEGREES_TO_RADIANS)) * this.sitScalar;
        this.front_leg_right_bottom.rotateAngleZ = 3 * DEGREES_TO_RADIANS * this.sitScalar;
        this.front_leg_left_bottom.rotateAngleX = (float) (Math.PI - (25 * DEGREES_TO_RADIANS)) * this.sitScalar;
        this.front_leg_left_bottom.rotateAngleZ = -3 * DEGREES_TO_RADIANS * this.sitScalar;
      }

      // Back Leg
      {
        this.back_leg_right.rotateAngleX = (float) (-Math.PI * 0.5f + (15 * DEGREES_TO_RADIANS)) * this.sitScalar;
        this.back_leg_left.rotateAngleX = (float) (-Math.PI * 0.5f + (15 * DEGREES_TO_RADIANS)) * this.sitScalar;
      }

      // Back Leg Bottom
      {
        // In this one, we swing the bottom legs out a little bit on the Z axis
        // to avoid some Z-fighting, plus it looks better to be able to see that
        // part of the leg.
        this.back_leg_right_bottom.rotateAngleX = (float) (Math.PI - (25 * DEGREES_TO_RADIANS)) * this.sitScalar;
        this.back_leg_right_bottom.rotateAngleZ = 3 * DEGREES_TO_RADIANS * this.sitScalar;
        this.back_leg_left_bottom.rotateAngleX = (float) (Math.PI - (25 * DEGREES_TO_RADIANS)) * this.sitScalar;
        this.back_leg_left_bottom.rotateAngleZ = -3 * DEGREES_TO_RADIANS * this.sitScalar;
      }

    } else {

      // If the entity is not sitting down or standing up, use the extremes.
      this.sitScalar = camelEntity.isSitting() ? 1 : 0;

      // Reset the leg bottom Z axes.
      this.front_leg_right_bottom.rotateAngleZ = 0;
      this.front_leg_left_bottom.rotateAngleZ = 0;
      this.back_leg_right_bottom.rotateAngleZ = 0;
      this.back_leg_left_bottom.rotateAngleZ = 0;
    }
  }

  @Override
  public void setRotationAngles(@Nonnull CamelEntity camelEntity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    // This is used to constantly play the walk animation.
    if (CamelEntity.DEBUG_WALK_ANIMATION) {
      limbSwing = Minecraft.getInstance().world.getGameTime() * 0.1f;

      // Limb swing amount while walking: 0.12960301
      // Limb swing amount while scared: 0.3798597
      // Limb swing amount while running at full-speed while leashed: 0.99391425
      limbSwingAmount = 0.12960301f;
    }

    // Animation period modifier
    limbSwing *= 0.6662f;

    // Animation amplitude modifier
    limbSwingAmount = Math.min(0.75f, limbSwingAmount * 2.8f);

    // -------------------------------------------------------------------------
    // - Look
    // -------------------------------------------------------------------------

    // Only want to look around if not eating.
    if (camelEntity.getEatDrinkTimer() <= 0) {
      this.head.rotateAngleX = headPitch * DEGREES_TO_RADIANS;
      this.neck_top.rotateAngleY = netHeadYaw * DEGREES_TO_RADIANS * 0.5f;
    }

    // Only want to play the walking animation below if not actively sitting
    // or standing.
    if ((camelEntity.isSitting() && camelEntity.getSitTimer() >= 0)
        || (!camelEntity.isSitting() && camelEntity.getSitTimer() < 20)) {
      return;
    }

    // -------------------------------------------------------------------------
    // - Walking Animation
    // -------------------------------------------------------------------------

    // Only move the neck if not eating.
    if (camelEntity.getEatDrinkTimer() <= 0) {
      float cos = -(MathHelper.cos((limbSwing + PI) * 2 - 1) * 2);
      this.neck_top.rotateAngleX = cos * limbSwingAmount * 0.015625f;
      this.head.rotateAngleX = -this.neck_top.rotateAngleX;
    }

    // Front
    {
      float cos = MathHelper.cos(limbSwing + PI);
      this.front_leg_right.rotateAngleX = cos * limbSwingAmount * 0.5f;
    }
    {
      float cos = MathHelper.cos(limbSwing);
      this.front_leg_left.rotateAngleX = cos * limbSwingAmount * 0.5f;
    }

    // Front bottom
    {
      float cos = MathHelper.cos(limbSwing + HALF_PI);
      this.front_leg_right_bottom.rotateAngleX = Math.max(0, cos) * limbSwingAmount;
    }
    {
      float cos = MathHelper.cos(limbSwing + PI + HALF_PI);
      this.front_leg_left_bottom.rotateAngleX = Math.max(0, cos) * limbSwingAmount;
    }

    float back_leg_time_offset = (float) (Math.PI * 0.25f);

    // Back
    {
      float cos = MathHelper.cos(limbSwing + back_leg_time_offset);
      this.back_leg_left.rotateAngleX = cos * limbSwingAmount * 0.5f;
    }
    {
      float cos = MathHelper.cos(limbSwing + PI + back_leg_time_offset);
      this.back_leg_right.rotateAngleX = cos * limbSwingAmount * 0.5f;
    }

    // Back Bottom
    {
      float sin = MathHelper.sin(limbSwing - PI + back_leg_time_offset);
      this.back_leg_right_bottom.rotateAngleX = -Math.max(0, sin) * limbSwingAmount * 0.75f;
    }
    {
      float sin = MathHelper.sin(limbSwing + back_leg_time_offset);
      this.back_leg_left_bottom.rotateAngleX = -Math.max(0, sin) * limbSwingAmount * 0.75f;
    }
  }

  @Override
  public void render(@Nonnull MatrixStack matrixStack, @Nonnull IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {

    if (this.isChild) {

      // Draw the child with a larger head.
      matrixStack.push();
      matrixStack.scale(0.6f, 0.5f, 0.6f);
      matrixStack.translate(0, 1.725, 0.22);
      this.neck.render(matrixStack, buffer, packedLight, packedOverlay);
      matrixStack.pop();

      matrixStack.push();
      matrixStack.scale(0.45454544f, 0.41322312f, 0.45454544f);
      matrixStack.translate(0, 2.0625, 0);
      this.front_leg_right.render(matrixStack, buffer, packedLight, packedOverlay);
      this.front_leg_left.render(matrixStack, buffer, packedLight, packedOverlay);
      this.back_leg_right.render(matrixStack, buffer, packedLight, packedOverlay);
      this.back_leg_left.render(matrixStack, buffer, packedLight, packedOverlay);
      this.tail.render(matrixStack, buffer, packedLight, packedOverlay);
      this.hump.render(matrixStack, buffer, packedLight, packedOverlay);
      this.body.render(matrixStack, buffer, packedLight, packedOverlay);
      matrixStack.pop();

    } else {

      // sitScalar * sitScalar is used here to keep the model's feet above
      // ground when actively sitting down or standing up.

      matrixStack.push();
      matrixStack.translate(0, 0.85 * this.sitScalar * this.sitScalar, 0);
      this.front_leg_right.render(matrixStack, buffer, packedLight, packedOverlay);
      this.front_leg_left.render(matrixStack, buffer, packedLight, packedOverlay);
      this.back_leg_right.render(matrixStack, buffer, packedLight, packedOverlay);
      this.back_leg_left.render(matrixStack, buffer, packedLight, packedOverlay);
      this.neck.render(matrixStack, buffer, packedLight, packedOverlay);
      this.tail.render(matrixStack, buffer, packedLight, packedOverlay);
      this.hump.render(matrixStack, buffer, packedLight, packedOverlay);
      this.body.render(matrixStack, buffer, packedLight, packedOverlay);
      matrixStack.pop();
    }
  }
}
