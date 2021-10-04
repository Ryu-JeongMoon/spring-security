package io.security.springsecurity.controller.admin;


import io.security.springsecurity.domain.dto.ResourcesDto;
import io.security.springsecurity.domain.entity.Resources;
import io.security.springsecurity.domain.entity.Role;
import io.security.springsecurity.repository.RoleRepository;
import io.security.springsecurity.service.ResourcesService;
import io.security.springsecurity.service.RoleService;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class ResourcesController {

    private final ResourcesService resourcesService;
    private final RoleRepository roleRepository;
    //    private final MethodSecurityService methodSecurityService;
    private final RoleService roleService;

    @GetMapping(value = "/admin/resources")
    public String getResources(Model model) {
        List<Resources> resources = resourcesService.selectResources();
        model.addAttribute("resources", resources);

        return "admin/resource/list";
    }

    @PostMapping(value = "/admin/resources")
    public String createResources(ResourcesDto resourcesDto) throws Exception {

        ModelMapper modelMapper = new ModelMapper();
        Role role = roleRepository.findByRoleName(resourcesDto.getRoleName());
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        Resources resources = modelMapper.map(resourcesDto, Resources.class);
        resources.setRoleSet(roles);

        resourcesService.insertResources(resources);
        //methodSecurityService.addMethodSecured(resourcesDto.getResourceName(), resourcesDto.getRoleName());

        return "redirect:/admin/resources";
    }

    @GetMapping(value = "/admin/resources/register")
    public String viewRoles(Model model) {
        List<Role> roleList = roleService.getRoles();
        model.addAttribute("roleList", roleList);
        Resources resources = new Resources();
        model.addAttribute("resources", resources);

        return "admin/resource/detail";
    }

    @GetMapping(value = "/admin/resources/{id}")
    public String getResources(@PathVariable Long id, Model model) {
        List<Role> roleList = roleService.getRoles();
        model.addAttribute("roleList", roleList);
        Resources resources = resourcesService.selectResources(id);
        model.addAttribute("resources", resources);

        return "admin/resource/detail";
    }

    @GetMapping(value = "/admin/resources/delete/{id}")
    public String removeResources(@PathVariable Long id) throws Exception {
        Resources resources = resourcesService.selectResources(id);
        resourcesService.deleteResources(id);
        //methodSecurityService.removeMethodSecured(resources.getResourceName());

        return "redirect:/admin/resources";
    }
}
