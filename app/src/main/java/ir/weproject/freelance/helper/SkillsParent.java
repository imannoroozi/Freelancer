package ir.weproject.freelance.helper;

import ir.weproject.freelance.ir.weproject.poem.objects.Skill;

/**
 * Created by Iman on 3/30/2016.
 */
public interface SkillsParent {

    public void skillClicked(Skill skill, int position);

    public void removeMySkill(Skill skill);
}
