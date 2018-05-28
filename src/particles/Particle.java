package particles;

import org.joml.Vector3f;

/**
 * Created by Bailey on 5/20/2018.
 */
public class Particle {
    Vector3f position;
    Vector3f velocity;
    Vector3f acceleration;

    int initialLifespan;
    int lifespan;

    public Particle(Vector3f pos, int lifespan){
        this.position = pos;
        this.initialLifespan = lifespan;
        this.lifespan = (int)(initialLifespan * Math.random());
        this.velocity = new Vector3f((float)(Math.random() * 2.0f - 1.0f), 1.0f, 0);
        this.acceleration = new Vector3f(0.0f, 0.0f, 0);
//        this.acceleration =((new Vector3f((float)Math.random(), (float)Math.random(), (float)Math.random()).mul(2.0f)).sub(new Vector3f(1, 1, 1))).mul(0.01f);
    }

    public float getRemainingLifetime(){
        return ((float)lifespan / (float)initialLifespan);
    }

    public void resetVelocity(){
        this.velocity = new Vector3f((float)(Math.random() * 2.0f - 1.0f), 0.1f, 0.0f);
        this.acceleration = new Vector3f(0.0f, 0.0f, 0);
    }
}
