document.addEventListener('DOMContentLoaded', function () {
  console.log('console is loading')

  const viewContactModal = document.getElementById('view_contact_modal')

  // options with default values
  const options = {
    placement: 'center',
    backdrop: 'dynamic',
    backdropClasses: 'bg-gray-900/50 dark:bg-gray-900/80 fixed inset-0 z-40',
    closable: true,
    onHide: () => {
      console.log('modal is hidden')
    },
    onShow: () => {
      console.log('modal is shown')
    },
    onToggle: () => {
      console.log('modal has been toggled')
    },
  }

  // instance options object
  const instanceOptions = {
    id: 'view_contact_modal',
    override: true,
  }

  const contactModal = new Modal(viewContactModal, options, instanceOptions)

  window.openContactModal = function () {
    contactModal.show()
  }

  window.closeContactModal = function () {
    contactModal.hide()
  }
  window.loadContactData = async function (id) {
    console.log(id)
    try {
      const data = await (
        await fetch(`http://localhost:8080/api/contacts/${id}`)
      ).json()
      document.querySelector('#contact_name').innerHTML = data.name
      document.querySelector('#contact_address').innerHTML = data.address
      document.querySelector('#contact_email').innerHTML = data.email

      document.querySelector('#contact_phone').innerHTML = data.phoneNumber
      document.querySelector('#Social_Link_1').href = data.websiteLink
      document.querySelector('#Social_Link_1').innerHTML = data.websiteLink
      document.querySelector('#Social_Link_2').href = data.linkedInLink
      document.querySelector('#Social_Link_2').innerHTML = data.linkedInLink
      document.querySelector('#description').innerHTML = data.description
      document.querySelector('#favourite').innerHTML = data.favourite
        ? '⭐'
        : '☆'
      document.querySelector('#contact_pic').src = data.picture
      openContactModal()
    } catch (error) {
      console.log(error)
    }
  }
})
