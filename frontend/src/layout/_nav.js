import React from 'react'
import CIcon from '@coreui/icons-react'

export default [
  {
    _tag: 'CSidebarNavItem',
    name: 'Dashboard',
    to: '/dashboard',
    icon: <CIcon name="cil-speedometer" customClasses="c-sidebar-nav-icon" />,
    badge: {
      color: 'info',
      text: 'NEW',
    }
  },
  // {
  //   _tag: 'CSidebarNavTitle',
  //   _children: ['Theme']
  // },
  // {
  //   _tag: 'CSidebarNavItem',
  //   name: 'Colors',
  //   to: '/theme/colors',
  //   icon: 'cil-drop',
  // },
  // {
  //   _tag: 'CSidebarNavItem',
  //   name: 'Typography',
  //   to: '/theme/typography',
  //   icon: 'cil-pencil',
  // },
  // {
  //   _tag: 'CSidebarNavItem',
  //   name: 'Map',
  //   to: '/route/map',
  //   icon: 'cil-map',
  // },
  // {
  //   _tag: 'CSidebarNavTitle',
  //   _children: ['Components']
  // },
  // {
  //   _tag: 'CSidebarNavDropdown',
  //   name: 'Menu',
  //   icon: 'cil-menu',
  //   _children: [{
  //     _tag: 'CSidebarNavDropdown',
  //     name: 'Base',
  //     route: '/base',
  //     icon: 'cil-puzzle',
  //     _children: [
  //       {
  //         _tag: 'CSidebarNavItem',
  //         name: 'Breadcrumb',
  //         to: '/base/breadcrumbs',
  //       },
  //       {
  //         _tag: 'CSidebarNavItem',
  //         name: 'Cards',
  //         to: '/base/cards',
  //       },
  //       {
  //         _tag: 'CSidebarNavItem',
  //         name: 'Carousel',
  //         to: '/base/carousels',
  //       },
  //       {
  //         _tag: 'CSidebarNavItem',
  //         name: 'Collapse',
  //         to: '/base/collapses',
  //       },
  //       {
  //         _tag: 'CSidebarNavItem',
  //         name: 'Forms',
  //         to: '/base/forms',
  //       },
  //       {
  //         _tag: 'CSidebarNavItem',
  //         name: 'Jumbotron',
  //         to: '/base/jumbotrons',
  //       },
  //       {
  //         _tag: 'CSidebarNavItem',
  //         name: 'List group',
  //         to: '/base/list-groups',
  //       },
  //       {
  //         _tag: 'CSidebarNavItem',
  //         name: 'Navs',
  //         to: '/base/navs',
  //       },
  //       {
  //         _tag: 'CSidebarNavItem',
  //         name: 'Navbars',
  //         to: '/base/navbars',
  //       },
  //       {
  //         _tag: 'CSidebarNavItem',
  //         name: 'Pagination',
  //         to: '/base/paginations',
  //       },
  //       {
  //         _tag: 'CSidebarNavItem',
  //         name: 'Popovers',
  //         to: '/base/popovers',
  //       },
  //       {
  //         _tag: 'CSidebarNavItem',
  //         name: 'Progress',
  //         to: '/base/progress-bar',
  //       },
  //       {
  //         _tag: 'CSidebarNavItem',
  //         name: 'Switches',
  //         to: '/base/switches',
  //       },
  //       {
  //         _tag: 'CSidebarNavItem',
  //         name: 'Tables',
  //         to: '/base/tables',
  //       },
  //       {
  //         _tag: 'CSidebarNavItem',
  //         name: 'Tabs',
  //         to: '/base/tabs',
  //       },
  //       {
  //         _tag: 'CSidebarNavItem',
  //         name: 'Tooltips',
  //         to: '/base/tooltips',
  //       },
  //     ],
  //   },
  //   {
  //     _tag: 'CSidebarNavDropdown',
  //     name: 'Buttons',
  //     route: '/buttons',
  //     icon: 'cil-cursor',
  //     _children: [
  //       {
  //         _tag: 'CSidebarNavItem',
  //         name: 'Buttons',
  //         to: '/buttons/buttons',
  //       },
  //       {
  //         _tag: 'CSidebarNavItem',
  //         name: 'Brand buttons',
  //         to: '/buttons/brand-buttons',
  //       },
  //       {
  //         _tag: 'CSidebarNavItem',
  //         name: 'Buttons groups',
  //         to: '/buttons/button-groups',
  //       },
  //       {
  //         _tag: 'CSidebarNavItem',
  //         name: 'Dropdowns',
  //         to: '/buttons/button-dropdowns',
  //       }
  //     ],
  //   },
  //   {
  //     _tag: 'CSidebarNavItem',
  //     name: 'Charts',
  //     to: '/charts',
  //     icon: 'cil-chart-pie'
  //   },
  //   {
  //     _tag: 'CSidebarNavDropdown',
  //     name: 'Icons',
  //     route: '/icons',
  //     icon: 'cil-star',
  //     _children: [
  //       {
  //         _tag: 'CSidebarNavItem',
  //         name: 'CoreUI Free',
  //         to: '/icons/coreui-icons',
  //         badge: {
  //           color: 'success',
  //           text: 'NEW',
  //         },
  //       },
  //       {
  //         _tag: 'CSidebarNavItem',
  //         name: 'CoreUI Flags',
  //         to: '/icons/flags',
  //       },
  //       {
  //         _tag: 'CSidebarNavItem',
  //         name: 'CoreUI Brands',
  //         to: '/icons/brands',
  //       },
  //     ],
  //   },
  //   {
  //     _tag: 'CSidebarNavDropdown',
  //     name: 'Notifications',
  //     route: '/notifications',
  //     icon: 'cil-bell',
  //     _children: [
  //       {
  //         _tag: 'CSidebarNavItem',
  //         name: 'Alerts',
  //         to: '/notifications/alerts',
  //       },
  //       {
  //         _tag: 'CSidebarNavItem',
  //         name: 'Badges',
  //         to: '/notifications/badges',
  //       },
  //       {
  //         _tag: 'CSidebarNavItem',
  //         name: 'Modal',
  //         to: '/notifications/modals',
  //       },
  //       {
  //         _tag: 'CSidebarNavItem',
  //         name: 'Toaster',
  //         to: '/notifications/toaster'
  //       }
  //     ]
  //   },
  //   {
  //     _tag: 'CSidebarNavItem',
  //     name: 'Widgets',
  //     to: '/widgets',
  //     icon: 'cil-calculator',
  //     badge: {
  //       color: 'info',
  //       text: 'NEW',
  //     },
  //   },
  //   {
  //     _tag: 'CSidebarNavDivider'
  //   },
  //   {
  //     _tag: 'CSidebarNavTitle',
  //     _children: ['Extras'],
  //   },
  //   {
  //     _tag: 'CSidebarNavDropdown',
  //     name: 'Pages',
  //     route: '/pages',
  //     icon: 'cil-star',
  //     _children: [
  //       {
  //         _tag: 'CSidebarNavItem',
  //         name: 'Login',
  //         to: '/login',
  //       },
  //       {
  //         _tag: 'CSidebarNavItem',
  //         name: 'Register',
  //         to: '/register',
  //       },
  //       {
  //         _tag: 'CSidebarNavItem',
  //         name: 'Error 404',
  //         to: '/404',
  //       },
  //       {
  //         _tag: 'CSidebarNavItem',
  //         name: 'Error 500',
  //         to: '/500',
  //       },
  //     ],
  //   },
  //   {
  //     _tag: 'CSidebarNavItem',
  //     name: 'Disabled',
  //     icon: 'cil-ban',
  //     badge: {
  //       color: 'secondary',
  //       text: 'NEW',
  //     },
  //     addLinkClass: 'c-disabled',
  //     'disabled': true
  //   },
  //   {
  //     _tag: 'CSidebarNavDivider',
  //     className: 'm-2'
  //   },
  //   {
  //     _tag: 'CSidebarNavTitle',
  //     _children: ['Labels']
  //   },
  //   {
  //     _tag: 'CSidebarNavItem',
  //     name: 'Label danger',
  //     to: '',
  //     icon: {
  //       name: 'cil-star',
  //       className: 'text-danger'
  //     },
  //     label: true
  //   },
  //   {
  //     _tag: 'CSidebarNavItem',
  //     name: 'Label info',
  //     to: '',
  //     icon: {
  //       name: 'cil-star',
  //       className: 'text-info'
  //     },
  //     label: true
  //   },
  //   {
  //     _tag: 'CSidebarNavItem',
  //     name: 'Label warning',
  //     to: '',
  //     icon: {
  //       name: 'cil-star',
  //       className: 'text-warning'
  //     },
  //     label: true
  //   },
  //   {
  //     _tag: 'CSidebarNavDivider',
  //     className: 'm-2'
  //   }],
  // },
  {
    _tag: 'CSidebarNavItem',
    name: 'Orders Information',
    to: '/orders',
    icon: <CIcon name="cil-notes" customClasses="c-sidebar-nav-icon" />,
  },
  {
    _tag: 'CSidebarNavItem',
    name: 'Depots Information',
    to: '/depots',
    icon: <CIcon name="cil-factory" customClasses="c-sidebar-nav-icon" />,
  },
  {
    _tag: 'CSidebarNavItem',
    name: 'Vehicles Information',
    to: '/vehicles',
    icon: <CIcon name="cil-truck" customClasses="c-sidebar-nav-icon" />,
  },
  {
    _tag: 'CSidebarNavItem',
    name: 'Customers Information',
    to: '/customers',
    icon: <CIcon name="cil-address-book" customClasses="c-sidebar-nav-icon" />,
  },
  {
    _tag: 'CSidebarNavItem',
    name: 'Products Information',
    to: '/products',
    icon: <CIcon name="cil-basket" customClasses="c-sidebar-nav-icon" />,
  },
  {
    _tag: 'CSidebarNavItem',
    name: 'Group Product Information',
    to: '/goods-group',
    icon: <CIcon name="cil-basket" customClasses="c-sidebar-nav-icon" />,
  },
  // {
  //   _tag: 'CSidebarNavDropdown',
  //   name: 'Tùy chỉnh thông tin',
  //   icon: 'cil-info',
  //   _children: [
  //     {
  //       _tag: 'CSidebarNavItem',
  //       name: 'Thông tin đơn hàng',
  //       to: '/orders',
  //       icon: 'cil-notes',
  //     },
  //     // {
  //     //   _tag: 'CSidebarNavItem',
  //     //   name: '2. Thông tin đội xe',
  //     //   to: '/vehicles',
  //     //   icon: 'cil-truck',
  //     // },
  //     // {
  //     //   _tag: 'CSidebarNavItem',
  //     //   name: '3. Thông tin khách hàng',
  //     //   to: '/customers',
  //     //   icon: 'cil-address-book',
  //     // },
  //     // {
  //     //   _tag: 'CSidebarNavItem',
  //     //   name: '4. Thông tin kho hàng',
  //     //   to: '/depots',
  //     //   icon: 'cil-factory',
  //     // },    
  //     // {
  //     //   _tag: 'CSidebarNavItem',
  //     //   name: '5. Thông tin sản phẩm',
  //     //   to: '/products',
  //     //   icon: 'cil-basket',
  //     // },
  //     // {
  //     //   _tag: 'CSidebarNavItem',
  //     //   name: '6. Thông tin nhóm hàng',
  //     //   to: '/goods-group',
  //     //   icon: 'cil-basket',
  //     // },
  //   ],
  // },
  {
    _tag: 'CSidebarNavDropdown',
    name: 'Schedule',
    icon: 'cil-paper-plane',
    _children: [
      {
        _tag: 'CSidebarNavItem',
        name: '1. Route initialization',
        to: '/routes-init',
        icon: 'cil-graph',
      },
      {
        _tag: 'CSidebarNavItem',
        name: '2. Route list',
        to: '/routes/solutions',
        icon: 'cil-settings',
      },
      
    ],
  },

  // {
  //   _tag: 'CSidebarNavDropdown',
  //   name: 'Base',
  //   route: '/base',
  //   icon: 'cil-puzzle',
  //   _children: [
  //     {
  //       _tag: 'CSidebarNavItem',
  //       name: 'Breadcrumb',
  //       to: '/base/breadcrumbs',
  //     },
  //     {
  //       _tag: 'CSidebarNavItem',
  //       name: 'Cards',
  //       to: '/base/cards',
  //     },
  //     {
  //       _tag: 'CSidebarNavItem',
  //       name: 'Carousel',
  //       to: '/base/carousels',
  //     },
  //     {
  //       _tag: 'CSidebarNavItem',
  //       name: 'Collapse',
  //       to: '/base/collapses',
  //     },
  //     {
  //       _tag: 'CSidebarNavItem',
  //       name: 'Forms',
  //       to: '/base/forms',
  //     },
  //     {
  //       _tag: 'CSidebarNavItem',
  //       name: 'Jumbotron',
  //       to: '/base/jumbotrons',
  //     },
  //     {
  //       _tag: 'CSidebarNavItem',
  //       name: 'List group',
  //       to: '/base/list-groups',
  //     },
  //     {
  //       _tag: 'CSidebarNavItem',
  //       name: 'Navs',
  //       to: '/base/navs',
  //     },
  //     {
  //       _tag: 'CSidebarNavItem',
  //       name: 'Navbars',
  //       to: '/base/navbars',
  //     },
  //     {
  //       _tag: 'CSidebarNavItem',
  //       name: 'Pagination',
  //       to: '/base/paginations',
  //     },
  //     {
  //       _tag: 'CSidebarNavItem',
  //       name: 'Popovers',
  //       to: '/base/popovers',
  //     },
  //     {
  //       _tag: 'CSidebarNavItem',
  //       name: 'Progress',
  //       to: '/base/progress-bar',
  //     },
  //     {
  //       _tag: 'CSidebarNavItem',
  //       name: 'Switches',
  //       to: '/base/switches',
  //     },
  //     {
  //       _tag: 'CSidebarNavItem',
  //       name: 'Tables',
  //       to: '/base/tables',
  //     },
  //     {
  //       _tag: 'CSidebarNavItem',
  //       name: 'Tabs',
  //       to: '/base/tabs',
  //     },
  //     {
  //       _tag: 'CSidebarNavItem',
  //       name: 'Tooltips',
  //       to: '/base/tooltips',
  //     },
  //   ],
  // },
  // {
  //   _tag: 'CSidebarNavDropdown',
  //   name: 'Buttons',
  //   route: '/buttons',
  //   icon: 'cil-cursor',
  //   _children: [
  //     {
  //       _tag: 'CSidebarNavItem',
  //       name: 'Buttons',
  //       to: '/buttons/buttons',
  //     },
  //     {
  //       _tag: 'CSidebarNavItem',
  //       name: 'Brand buttons',
  //       to: '/buttons/brand-buttons',
  //     },
  //     {
  //       _tag: 'CSidebarNavItem',
  //       name: 'Buttons groups',
  //       to: '/buttons/button-groups',
  //     },
  //     {
  //       _tag: 'CSidebarNavItem',
  //       name: 'Dropdowns',
  //       to: '/buttons/button-dropdowns',
  //     }
  //   ],
  // },
  // {
  //   _tag: 'CSidebarNavItem',
  //   name: 'Charts',
  //   to: '/charts',
  //   icon: 'cil-chart-pie'
  // },
  // {
  //   _tag: 'CSidebarNavDropdown',
  //   name: 'Icons',
  //   route: '/icons',
  //   icon: 'cil-star',
  //   _children: [
  //     {
  //       _tag: 'CSidebarNavItem',
  //       name: 'CoreUI Free',
  //       to: '/icons/coreui-icons',
  //       badge: {
  //         color: 'success',
  //         text: 'NEW',
  //       },
  //     },
  //     {
  //       _tag: 'CSidebarNavItem',
  //       name: 'CoreUI Flags',
  //       to: '/icons/flags',
  //     },
  //     {
  //       _tag: 'CSidebarNavItem',
  //       name: 'CoreUI Brands',
  //       to: '/icons/brands',
  //     },
  //   ],
  // },
  // {
  //   _tag: 'CSidebarNavDropdown',
  //   name: 'Notifications',
  //   route: '/notifications',
  //   icon: 'cil-bell',
  //   _children: [
  //     {
  //       _tag: 'CSidebarNavItem',
  //       name: 'Alerts',
  //       to: '/notifications/alerts',
  //     },
  //     {
  //       _tag: 'CSidebarNavItem',
  //       name: 'Badges',
  //       to: '/notifications/badges',
  //     },
  //     {
  //       _tag: 'CSidebarNavItem',
  //       name: 'Modal',
  //       to: '/notifications/modals',
  //     },
  //     {
  //       _tag: 'CSidebarNavItem',
  //       name: 'Toaster',
  //       to: '/notifications/toaster'
  //     }
  //   ]
  // },
  // {
  //   _tag: 'CSidebarNavItem',
  //   name: 'Widgets',
  //   to: '/widgets',
  //   icon: 'cil-calculator',
  //   badge: {
  //     color: 'info',
  //     text: 'NEW',
  //   },
  // },
  // {
  //   _tag: 'CSidebarNavDivider'
  // },
  // {
  //   _tag: 'CSidebarNavTitle',
  //   _children: ['Extras'],
  // },
  // {
  //   _tag: 'CSidebarNavDropdown',
  //   name: 'Pages',
  //   route: '/pages',
  //   icon: 'cil-star',
  //   _children: [
  //     {
  //       _tag: 'CSidebarNavItem',
  //       name: 'Login',
  //       to: '/login',
  //     },
  //     {
  //       _tag: 'CSidebarNavItem',
  //       name: 'Register',
  //       to: '/register',
  //     },
  //     {
  //       _tag: 'CSidebarNavItem',
  //       name: 'Error 404',
  //       to: '/404',
  //     },
  //     {
  //       _tag: 'CSidebarNavItem',
  //       name: 'Error 500',
  //       to: '/500',
  //     },
  //   ],
  // },
  // {
  //   _tag: 'CSidebarNavItem',
  //   name: 'Disabled',
  //   icon: 'cil-ban',
  //   badge: {
  //     color: 'secondary',
  //     text: 'NEW',
  //   },
  //   addLinkClass: 'c-disabled',
  //   'disabled': true
  // },
  // {
  //   _tag: 'CSidebarNavDivider',
  //   className: 'm-2'
  // },
  // {
  //   _tag: 'CSidebarNavTitle',
  //   _children: ['Labels']
  // },
  // {
  //   _tag: 'CSidebarNavItem',
  //   name: 'Label danger',
  //   to: '',
  //   icon: {
  //     name: 'cil-star',
  //     className: 'text-danger'
  //   },
  //   label: true
  // },
  // {
  //   _tag: 'CSidebarNavItem',
  //   name: 'Label info',
  //   to: '',
  //   icon: {
  //     name: 'cil-star',
  //     className: 'text-info'
  //   },
  //   label: true
  // },
  // {
  //   _tag: 'CSidebarNavItem',
  //   name: 'Label warning',
  //   to: '',
  //   icon: {
  //     name: 'cil-star',
  //     className: 'text-warning'
  //   },
  //   label: true
  // },
  // {
  //   _tag: 'CSidebarNavDivider',
  //   className: 'm-2'
  // }
]

