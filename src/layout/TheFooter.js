import React from 'react'
import { CFooter } from '@coreui/react'

const TheFooter = () => {
  return (
    <CFooter fixed={false}>
      <div>
        <a href="https://github.com/zovivo" target="_blank" rel="noopener noreferrer">zovivo</a>
        <span className="ml-1">&copy; 2021 graduation research.</span>
      </div>
      <div className="mfs-auto">
        <span className="mr-1">Designed by</span>
        <a href="https://github.com/zovivo" target="_blank" rel="noopener noreferrer">zovivo</a>
      </div>
    </CFooter>
  )
}

export default React.memo(TheFooter)
