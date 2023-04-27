package com.scfg.core.adapter.persistence.menu;

import com.scfg.core.adapter.persistence.action.ActionJpaEntity;
import com.scfg.core.adapter.persistence.action.ActionPersistenceAdapter;
import com.scfg.core.adapter.persistence.action.ActionRepository;
import com.scfg.core.application.port.out.MenuPort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.common.enums.PersistenceStatusEnum;
import com.scfg.core.common.exception.NotDataFoundException;
import com.scfg.core.domain.common.Menu;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@PersistenceAdapter
@RequiredArgsConstructor
public class MenuPersistenceAdapter implements MenuPort {

    private final MenuRepository menuRepository;
    private final ActionRepository actionRepository;

    @Override
    public List<Menu> findAll() {
        List<Menu> menus = new ArrayList<>();
        List<MenuJpaEntity> menusJpaEntity = menuRepository.findAll();
        menusJpaEntity.forEach(m ->{
            m.setSubMenu(new ArrayList<>());
            menus.add(mapToDomain(m));
        });
        return menus;
    }

    @Override
    public List<Menu> findAllWithDetail() {
        List<Menu> menus = new ArrayList<>();
        List<MenuJpaEntity> menusJpaEntity = menuRepository.findAllByParentIdIsNull();
        menusJpaEntity.forEach(m -> menus.add(mapToDomain(m)));
        return menus;
    }

    @Override
    public List<Menu> findAllByRoleId(long roleId) {
        List<Menu> menus = new ArrayList<>();
        List<MenuJpaEntity> menusJpaEntity = menuRepository.findAllByRoleId(roleId);
        menusJpaEntity.forEach(m -> {
            m.setSubMenu(new ArrayList<>());
            List<ActionJpaEntity> actions = actionRepository.findAllByRoleIdAndMenuId(roleId, m.getId());
            menus.add(mapToDomain(m, actions));
        });
        return menus;
    }

    @Override
    public Menu findById(long id) {
        MenuJpaEntity menuJpaEntity = menuRepository.findById(id).orElseThrow(() -> new NotDataFoundException("Menu: " + id + " No encontrado"));
        return mapToDomain(menuJpaEntity);
    }

    @Override
    public Boolean save(Menu menu) {

        MenuJpaEntity menuJpaEntity = mapToJpaEntityAll(menu);

        if (menu.getParentId() != null) {
            MenuJpaEntity parentMenu = menuRepository.findById(menu.getParentId()).orElseThrow(() -> new NotDataFoundException("Menu: " + menu.getParentId() + " No encontrado"));
            menuJpaEntity.setParentId(parentMenu);
        }

        MenuJpaEntity parentMenuJpaEntity = menuJpaEntity;
        menuJpaEntity.getSubMenu().forEach((m) -> {
            if (m.getName() == null || m.getName().equals("")) {
                m.setStatus(PersistenceStatusEnum.DELETED.getValue());
            }
            m.setParentId(parentMenuJpaEntity);
        });

        menuJpaEntity = menuRepository.save(menuJpaEntity);
        return menuJpaEntity != null;
    }

    // Este método se dejará de ocupar
    @Override
    public Menu update(Menu menu) {
        MenuJpaEntity menuJpaEntity = mapToJpaEntity(menu);
        // menuJpaEntity.setLastModifiedAt(LocalDateTime.now());
        menuJpaEntity = menuRepository.save(menuJpaEntity);
        return mapToDomain(menuJpaEntity);
    }

    @Override
    public Boolean delete(Menu menu) {
        menuRepository.deleteAllByIdOrParentId(PersistenceStatusEnum.DELETED.getValue(), menu.getId());
        return true;
    }

    //#region Mappers
    public static MenuJpaEntity mapToJpaEntity(Menu menu) {
        MenuJpaEntity menuJpaEntity = MenuJpaEntity.builder()
                .id(menu.getId())
                .name(menu.getName())
                .description(menu.getDescription())
                .url(menu.getUrl())
                .icon(menu.getIcon())
                .createdAt(menu.getCreatedAt())
                .lastModifiedAt(menu.getLastModifiedAt())
                .subMenu(new ArrayList<>())
                .build();

        return  menuJpaEntity;
    }

    private MenuJpaEntity mapToJpaEntityAll(Menu menu) {

        MenuJpaEntity menuJpaEntity = new MenuJpaEntity();

        if (menu.getSubMenu().size() <= 0) {
            menuJpaEntity = MenuJpaEntity.builder()
                    .id(menu.getId())
                    .name(menu.getName())
                    .description(menu.getDescription())
                    .url(menu.getUrl())
                    .icon(menu.getIcon())
                    .createdAt(menu.getCreatedAt())
                    .lastModifiedAt(menu.getLastModifiedAt())
                    .subMenu(new ArrayList<>())
                    // .parentId(menu.getParentId())
                    .build();
        }else{
            menuJpaEntity = MenuJpaEntity.builder()
                    .id(menu.getId())
                    .name(menu.getName())
                    .description(menu.getDescription())
                    .url(menu.getUrl())
                    .icon(menu.getIcon())
                    .createdAt(menu.getCreatedAt())
                    .lastModifiedAt(menu.getLastModifiedAt())
                    .subMenu(menu.getSubMenu()
                            .stream()
                            .map(m -> mapToJpaEntity(m))
                            .collect(Collectors.toList()))
                    //.parentId(new MenuJpaEntity())
                    .build();
        }

        return  menuJpaEntity;
    }

    private Menu mapToDomain(MenuJpaEntity menuJpaEntity) {

        Menu menu = new Menu();

        if (menuJpaEntity.getSubMenu().size() <= 0) {
            menu = Menu.builder()
                    .id(menuJpaEntity.getId())
                    .name(menuJpaEntity.getName())
                    .description(menuJpaEntity.getDescription())
                    .url(menuJpaEntity.getUrl())
                    .icon(menuJpaEntity.getIcon())
                    .createdAt(menuJpaEntity.getCreatedAt())
                    .lastModifiedAt(menuJpaEntity.getLastModifiedAt())
                    .parentId(menuJpaEntity.getParentId() != null ? menuJpaEntity.getParentId().getId() : null)
                    .subMenu(new ArrayList<>())
                    .build();
        } else {
            menu = Menu.builder()
                    .id(menuJpaEntity.getId())
                    .name(menuJpaEntity.getName())
                    .description(menuJpaEntity.getDescription())
                    .url(menuJpaEntity.getUrl())
                    .icon(menuJpaEntity.getIcon())
                    .createdAt(menuJpaEntity.getCreatedAt())
                    .lastModifiedAt(menuJpaEntity.getLastModifiedAt())
                    .parentId(menuJpaEntity.getParentId() != null ? menuJpaEntity.getParentId().getId() : null)
                    .subMenu(menuJpaEntity.getSubMenu()
                            .stream()
                            .map(m -> mapToDomain(m))
                            .collect(Collectors.toList()))
                    .build();
        }

        return menu;
    }

    private Menu mapToDomain(MenuJpaEntity menuJpaEntity, List<ActionJpaEntity> actionJpaEntityList) {

        Menu menu = Menu.builder()
                .id(menuJpaEntity.getId())
                .name(menuJpaEntity.getName())
                .description(menuJpaEntity.getDescription())
                .url(menuJpaEntity.getUrl())
                .icon(menuJpaEntity.getIcon())
                .createdAt(menuJpaEntity.getCreatedAt())
                .lastModifiedAt(menuJpaEntity.getLastModifiedAt())
                .parentId(menuJpaEntity.getParentId() != null ? menuJpaEntity.getParentId().getId() : null)
                .actions(actionJpaEntityList
                        .stream()
                        .map(a -> ActionPersistenceAdapter.mapToDomain(a))
                        .collect(Collectors.toList()))
                .build();

        if (menuJpaEntity.getSubMenu().size() <= 0) {
            menu.setSubMenu(new ArrayList<>());
        } else {
            menu.setSubMenu(menuJpaEntity.getSubMenu()
                    .stream()
                    .map(m -> mapToDomain(m))
                    .collect(Collectors.toList()));
        }

        return menu;
    }

    //#endregion
}
